package com.plato.recoserver.recoserver.common;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Kevin
 * @date 2022-03-17
 */
public class CandidateItem extends AbstractItem<Long> {
    /**
     * 召回渠道来源
     */
    @Getter
    @Setter
    private String source;

    @Getter
    @Setter
    private String strategy;

    @Getter
    @Setter
    private String trigger;

    @Getter
    @Setter
    private int Status = 1;

    @Getter
    @Setter
    private double rankScore;

    public enum FilterType {
        NORMAL,  // 没有过滤
        READ, // 已读过滤，候补
        SPECIFIED,  // 海洛图册特殊过滤
        SESSION,
        REGULATED
    };
    /**
     * 召回过滤类型
     */
    @Getter
    @Setter
    private FilterType filterType;


    @Getter
    @Setter
    private RetrievalInfo retrievalInfo;

    @Getter
    @Setter
    private RankInfo rankInfo;

    @Getter
    @Setter
    private ReRankInfo reRankInfo;

    public CandidateItem(Long id, int type, double score) {
        super(id, type, score);
        this.filterType = FilterType.NORMAL;
        this.strategy = "";
    }

    public CandidateItem(Long id, int type, double score, String trigger) {
        super(id, type, score);
        this.trigger = trigger;
        this.filterType = FilterType.NORMAL;
        this.strategy = "";
    }
    public String toString() {
        return new StringBuilder("RetrievalItem(id=").append(this.getId())
                .append(", type=").append(this.type)
                .append(", score=").append(this.getScore())
                .append(", rankScore=").append(this.rankScore)
                .append(", source=").append(this.source)
                .append(", filterType=").append(this.filterType)
                .append(", trigger=").append(this.trigger)
                .append(", strategy=").append(this.strategy)
                .append(")")
                .toString();
    }

}
