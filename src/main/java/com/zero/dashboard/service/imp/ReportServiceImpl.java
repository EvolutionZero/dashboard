package com.zero.dashboard.service.imp;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.service.additional.query.impl.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.additional.query.impl.QueryChainWrapper;
import com.zero.dashboard.component.DailyReporter;
import com.zero.dashboard.component.Reporter;
import com.zero.dashboard.dto.bo.VolumeInfo;
import com.zero.dashboard.dto.ctx.DailyReportContext;
import com.zero.dashboard.dto.ctx.VelocityParamsContext;
import com.zero.dashboard.dto.request.DailyReportRequest;
import com.zero.dashboard.dto.request.ScreenshotRequest;
import com.zero.dashboard.dto.response.ScreenshotResponse;
import com.zero.dashboard.entity.*;
import com.zero.dashboard.enums.ExtremeLevelEnum;
import com.zero.dashboard.enums.ExtremeTypeEnum;
import com.zero.dashboard.mapper.*;
import com.zero.dashboard.service.ReportService;
import com.zero.dashboard.utils.JsonUtils;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private Reporter reporter;

    @Autowired
    private DailyMapper dailyMapper;

    @Autowired
    private MacdMapper macdMapper;

    @Autowired
    private KdjMapper kdjMapper;

    @Autowired
    private ExtremeMapper extremeMapper;

    @Autowired
    private VolumeRatioBaseResultMapper volumeRatioBaseResultMapper;

    @Autowired
    private DailyReporter dailyReporter;

    @Override
    public ScreenshotResponse screenshot(ScreenshotRequest request) {
        return reporter.export(request);
    }

    @Override
    public void whole(DailyReportRequest request) {
        DailyReportContext ctx = new DailyReportContext();
        ctx.setCode(request.getCode());
        ctx.setName(request.getName());
        ctx.setFileName(request.getFileName());
        ctx.setBucketName(request.getBucketName());
        ctx.setObjectPath(request.getObjectPath());
        ctx.setTemplateName("velocity/reportTemplate.vm");
        ctx.setParams(generateWholeVelocityParams(new VelocityParamsContext(request)));
        dailyReporter.exec(ctx);
    }


    @Override
    public void kline(DailyReportRequest request) {
        DailyReportContext ctx = new DailyReportContext();
        ctx.setCode(request.getCode());
        ctx.setName(request.getName());
        ctx.setFileName(request.getFileName());
        ctx.setBucketName(request.getBucketName());
        ctx.setObjectPath(request.getObjectPath());
        ctx.setTemplateName("velocity/klineTemplate.vm");
        ctx.setParams(generateKLineVelocityParams(new VelocityParamsContext( request)));
        dailyReporter.exec(ctx);
    }

    private Map<String, Object> generateKLineVelocityParams(VelocityParamsContext ctx){
        int backoff = ctx.getBackoff();
        int forward = ctx.getForward();
        LocalDate focusDate = ctx.getFocusDate();
        Map<String, Object> params = new HashMap<>();
        LocalDate startDate = null;
        LocalDate endDate = null;
        String code = ctx.getCode();
        String name = ctx.getName();
        if(focusDate != null){
            startDate = dailyMapper.getBackoffTradeDate(code, focusDate, Math.abs(backoff));
            endDate = dailyMapper.getForwardTradeDate(code, focusDate, Math.abs(forward));
        }
        params.putAll(generateExtremeParams(startDate, endDate, code));
        params.putAll(generateKLineParams(startDate, endDate, code, focusDate));

        params.put("title", code + "_" + name + (focusDate != null ? "_" + focusDate.format(DateTimeFormatter.ISO_DATE) : ""));
        params.put("stockName", name);
        params.put("scriptHomePath", System.getProperty("script.home.path", "D:/IdeaProjects/eagle-eye/src/main/resources/velocity"));
        return params;
    }

    private Map<String, Object> generateWholeVelocityParams(VelocityParamsContext ctx){
        int backoff = ctx.getBackoff();
        int forward = ctx.getForward();
        LocalDate focusDate = ctx.getFocusDate();
        Map<String, Object> params = new HashMap<>();
        LocalDate startDate = null;
        LocalDate endDate = null;
        String code = ctx.getCode();
        if(focusDate != null){
            startDate = dailyMapper.getBackoffTradeDate(code, focusDate, Math.abs(backoff));
            endDate = dailyMapper.getForwardTradeDate(code, focusDate, Math.abs(forward));
        }
        params.putAll(generateExtremeParams(startDate, endDate, code));
        params.putAll(generateKLineParams(startDate, endDate, code, focusDate));
        params.putAll(generateKdjParams(startDate, endDate, code));
        params.putAll(generateMacdsParams(startDate, endDate, code));
        params.putAll(generateVolumeAndTurnoverParams(startDate, endDate, code));

        params.put("title", ctx.getCode() + "_" + ctx.getName() + (focusDate != null ? "_" + focusDate.format(DateTimeFormatter.ISO_DATE) : ""));
        params.put("stockName", ctx.getName());
        params.put("scriptHomePath", System.getProperty("script.home.path", "D:/IdeaProjects/eagle-eye/src/main/resources/velocity"));
        return params;
    }

    private Map<String, Object> generateVolumeAndTurnoverParams(LocalDate startDate, LocalDate endDate, String code) {
        List<VolumeInfo> volumeInfos = volumeRatioBaseResultMapper.listVolumeInfo(code, startDate, endDate);
        List<NodeData> volumeBarData = volumeInfos.stream()
                .map(v -> new NodeData(v.getTradingVolume(), (v.getClose().subtract(v.getOpen())).compareTo(new BigDecimal("0")) > 0 ? "red" : "green"))
                .collect(Collectors.toList());
        List<BigDecimal> volumeAvgs = volumeInfos.stream().map(v -> v.getAvg().setScale(2, BigDecimal.ROUND_HALF_UP)).collect(Collectors.toList());
        BigDecimal volumeMax = volumeInfos.stream().map(VolumeInfo::getTradingVolume).max(Comparator.naturalOrder()).get().multiply(new BigDecimal("1.1")).setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal volumeInterval = volumeMax.divide(new BigDecimal("10"), 2, BigDecimal.ROUND_HALF_UP);
        List<String> volumeDates = volumeInfos.stream()
                .map(VolumeInfo::getDate)
                .map(d -> "'" + d.format(DateTimeFormatter.ISO_DATE) + "'")
                .collect(Collectors.toList());

        List<BigDecimal> turnovers = volumeInfos.stream().map(v -> v.getTurnover().setScale(2, BigDecimal.ROUND_HALF_UP)).collect(Collectors.toList());
        BigDecimal turnoverMax = volumeInfos.stream().map(VolumeInfo::getTurnover).max(Comparator.naturalOrder()).get().multiply(new BigDecimal("1.1")).setScale(2, BigDecimal.ROUND_HALF_UP);
        turnoverMax = turnoverMax.compareTo(new BigDecimal("100")) > 0 ? new BigDecimal("100") : turnoverMax;
        BigDecimal turnoverInterval = turnoverMax.divide(new BigDecimal("10"), 2, BigDecimal.ROUND_HALF_UP);
        Map<String, Object> params = new HashMap<>();
        params.put("volumeDates", volumeDates.toString());
        params.put("volumeBarData", JsonUtils.stringify(volumeBarData));
        params.put("volumeAvgs", volumeAvgs.toString());
        params.put("volumeMax", volumeMax);
        params.put("volumeInterval", volumeInterval);

        params.put("turnovers", turnovers.toString());
        params.put("turnoverMax", turnoverMax);
        params.put("turnoverInterval", turnoverInterval);
        return params;
    }

    private Map<String, Object> generateMacdsParams(LocalDate startDate, LocalDate endDate, String code) {
        List<Macd> macds = new LambdaQueryChainWrapper<>(macdMapper)
                .eq(Macd::getCode, code)
                .ge(startDate != null, Macd::getDate, startDate)
                .le(endDate != null, Macd::getDate, endDate)
                .orderByAsc(Macd::getDate)
                .list();
        List<String> macdDates = macds.stream()
                .map(Macd::getDate)
                .map(d -> "'" + d.format(DateTimeFormatter.ISO_DATE) + "'")
                .collect(Collectors.toList());
        List<NodeData> macdNodeDatas = macds.stream().map(Macd::getMacd).map(NodeData::new).collect(Collectors.toList());

        List<BigDecimal> difs = macds.stream().map(Macd::getDif).collect(Collectors.toList());
        List<BigDecimal> deas = macds.stream().map(Macd::getDea).collect(Collectors.toList());

        List<BigDecimal> allMacdDatas = new LinkedList<>();
        allMacdDatas.addAll(difs);
        allMacdDatas.addAll(deas);
        allMacdDatas.addAll(macds.stream().map(Macd::getMacd).collect(Collectors.toList()));
        BigDecimal macdMax = allMacdDatas.stream().max(Comparator.naturalOrder()).get().multiply(new BigDecimal("1.1")).setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal macdMin = allMacdDatas.stream().min(Comparator.naturalOrder()).get().multiply(new BigDecimal("1.1")).setScale(2, BigDecimal.ROUND_HALF_UP);

        BigDecimal macdInterval = (macdMax.subtract(macdMin)).divide(new BigDecimal("10"), 2, BigDecimal.ROUND_HALF_UP);
        Map<String, Object> params = new HashMap<>();
        params.put("macdDates", macdDates.toString());
        params.put("macdNodeDatas", JsonUtils.stringify(macdNodeDatas));
        params.put("difs", difs.toString());
        params.put("deas", deas.toString());
        params.put("macdInterval", macdInterval);
        params.put("macdMax", macdMax);
        params.put("macdMin", macdMin);
        return params;
    }

    private Map<String, Object> generateKdjParams(LocalDate startDate, LocalDate endDate, String code) {
        List<Kdj> kdjs = new LambdaQueryChainWrapper<>(kdjMapper)
                .eq(Kdj::getCode, code)
                .ge(startDate != null, Kdj::getDate, startDate)
                .le(endDate != null, Kdj::getDate, endDate)
                .orderByAsc(Kdj::getDate)
                .list();
        List<String> kdjDates = kdjs.stream()
                .map(Kdj::getDate)
                .map(d -> "'" + d.format(DateTimeFormatter.ISO_DATE) + "'")
                .collect(Collectors.toList());
        List<NodeData> rsvNodeDatas = kdjs.stream().map(Kdj::getRsv).map(rsv -> new NodeData(rsv, "rgba(255, 0, 0,0.1)")).collect(Collectors.toList());
        List<BigDecimal> ks = kdjs.stream().map(Kdj::getK).collect(Collectors.toList());
        List<BigDecimal> ds = kdjs.stream().map(Kdj::getD).collect(Collectors.toList());
        List<BigDecimal> js = kdjs.stream().map(Kdj::getJ).collect(Collectors.toList());
        List<BigDecimal> allKdjDatas = new LinkedList<>();
        allKdjDatas.addAll(ks);
        allKdjDatas.addAll(ds);
        allKdjDatas.addAll(js);

        BigDecimal kdjMax = allKdjDatas.stream().max(Comparator.naturalOrder()).get().multiply(new BigDecimal("1.1")).setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal kdjMin = allKdjDatas.stream().min(Comparator.naturalOrder()).get().multiply(new BigDecimal("1.1")).setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal kdjInterval = (kdjMax.subtract(kdjMin)).divide(new BigDecimal("10"), 2, BigDecimal.ROUND_HALF_UP);
        Map<String, Object> params = new HashMap<>();
        params.put("kdjDates", kdjDates.toString());
        params.put("rsvNodeDatas", JsonUtils.stringify(rsvNodeDatas));
        params.put("ks", ks.toString());
        params.put("ds", ds.toString());
        params.put("js", js.toString());
        params.put("kdjInterval", kdjInterval);
        params.put("kdjMax", kdjMax);
        params.put("kdjMin", kdjMin);
        return params;
    }

    private Map<String, Object> generateKLineParams(LocalDate startDate, LocalDate endDate, String code, LocalDate focusDate) {
        List<Daily> dailies = new QueryChainWrapper<>(dailyMapper)
                .select("code", "date", "ln(open) as open", "ln(close) as close", "ln(low) as low", "ln(high) as high" , "trading_volume")
                .eq("code", code)
                .ge(startDate != null, "date", startDate)
                .le(endDate != null, "date", endDate)
                .orderByAsc("date")
                .list();
        List<List<Object>> kLineData = dailies.stream().map(Daily::toReportData).collect(Collectors.toList());
        String kLineDataStr = "\"" + JsonUtils.stringify(kLineData).replace("\"", "\\\"") + "\"";
        Map<String, Object> params = new HashMap<>();
        params.put("focusStartDate", startDate == null ? dailies.get(0).getDate().format(DateTimeFormatter.ISO_DATE) : dailyMapper.getForwardTradeDate(code, focusDate, 1).format(DateTimeFormatter.ISO_DATE));
        params.put("focusEndDate", endDate == null ? dailies.get(dailies.size() - 1).getDate().format(DateTimeFormatter.ISO_DATE) : endDate.format(DateTimeFormatter.ISO_DATE));
        params.put("klineDatas", kLineDataStr);
        return params;
    }

    private Map<String, Object> generateExtremeParams(LocalDate startDate, LocalDate endDate, String code) {
        //FIXME 暂时使用短期，可配置
        List<Extreme> extremes = new QueryChainWrapper<>(extremeMapper)
                .select("code", "date", "level", "type", "ln(value) as value")
                .eq("code", code)
                .eq("level", ExtremeLevelEnum.LONG.getValue())
                .ge(startDate != null, "date", startDate)
                .le(endDate != null, "date", endDate)
                .orderByAsc("date")
                .list();
        List<Extreme> bottomLine = extremes.stream().filter(e -> e.getType().equals(ExtremeTypeEnum.BOTTOM.getValue())).collect(Collectors.toList());
        List<Extreme> topLine = extremes.stream().filter(e -> e.getType().equals(ExtremeTypeEnum.TOP.getValue())).collect(Collectors.toList());
        List<List<ExtremeCoord>> markLineData = new LinkedList<>();
        if(CollectionUtil.isNotEmpty(bottomLine)){
            for (int i = 1; i < bottomLine.size(); i++) {
                Extreme pre = bottomLine.get(i - 1);
                Extreme cur = bottomLine.get(i);
                List<ExtremeCoord> line = new LinkedList<>();
                line.add(new ExtremeCoord(pre.getDate().format(DateTimeFormatter.ISO_DATE), pre.getValue(), null, "bottom", "red"));
                line.add(new ExtremeCoord(cur.getDate().format(DateTimeFormatter.ISO_DATE), cur.getValue(), cur.getValue(), "bottom","red"));

                markLineData.add(line);
            }
        }
        if(CollectionUtil.isNotEmpty(topLine)){
            for (int i = 1; i < topLine.size(); i++) {
                Extreme pre = topLine.get(i - 1);
                Extreme cur = topLine.get(i);
                List<ExtremeCoord> line = new LinkedList<>();
                line.add(new ExtremeCoord(pre.getDate().format(DateTimeFormatter.ISO_DATE), pre.getValue(), null, "top", "green"));
                line.add(new ExtremeCoord(cur.getDate().format(DateTimeFormatter.ISO_DATE), cur.getValue(), cur.getValue(), "top","green"));

                markLineData.add(line);
            }
        }
        Map<String, Object> params = new HashMap<>();
        params.put("markLineData", JsonUtils.stringify(markLineData));
        return params;
    }


    @Data
    public static class NodeData {
        private BigDecimal value;
        private ItemStyle itemStyle;

        public NodeData(BigDecimal value){
            this.value = value;
            this.itemStyle = new ItemStyle(value.compareTo(new BigDecimal("0")) > 0 ? "red" : "green");
        }

        public NodeData(BigDecimal value, String color){
            this.value = value;
            this.itemStyle = new ItemStyle(color);
        }

        @Data
        public static class ItemStyle {
            private String color;
            public ItemStyle(String color){
                this.color = color;
            }
        }
    }

    @Data
    public static class ExtremeCoord {
        private List<Object> coord;
        private BigDecimal value;
        private ExtremeCoord.ItemStyle itemStyle;

        private String type;

        public ExtremeCoord(Object x, Object y, BigDecimal value, String type, String color){
            List<Object> coord = new LinkedList<>();
            coord.add(x);
            coord.add(y);
            this.coord = coord;
            this.value = value;
            this.type = type;
            this.itemStyle = new ExtremeCoord.ItemStyle(color);
        }

        @Data
        public static class ItemStyle {
            private String color;
            public ItemStyle(String color){
                this.color = color;
            }
        }
    }

}
