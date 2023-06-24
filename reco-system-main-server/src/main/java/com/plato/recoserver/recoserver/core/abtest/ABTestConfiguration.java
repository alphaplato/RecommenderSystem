package com.plato.recoserver.recoserver.core.abtest;

import com.alibaba.fastjson.JSONObject;
import com.ctrip.framework.apollo.spring.annotation.ApolloJsonValue;
import com.plato.recoserver.recoserver.core.rerank.strategy.common.RestrictStrategy;
import com.plato.recoserver.recoserver.core.rerank.strategy.special.ArticleStrategy;
import org.springframework.beans.factory.annotation.Value;
import com.plato.recoserver.recoserver.core.context.RecommendConfiguration;
import com.plato.recoserver.recoserver.core.rerank.strategy.common.BreakInStrategy;
import com.plato.recoserver.recoserver.core.rerank.strategy.common.ReWeightStrategy;
import lombok.Getter;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author lishuguang
 * @date 2022/10/11
 **/
@Component
@RefreshScope
@Getter
public class ABTestConfiguration {
    // 以下为实验参数配置，可根据需求扩充、缩减
    @ApolloJsonValue("${reco.server.ab.retrieval.channels1:[]}")
    private List<RecommendConfiguration.ChannelConf> channelConfs1;

    @ApolloJsonValue("${reco.server.ab.retrieval.channels2:[]}")
    private List<RecommendConfiguration.ChannelConf> channelConfs2;

    @ApolloJsonValue("${reco.server.ab.retrieval.channels3:[]}")
    private List<RecommendConfiguration.ChannelConf> channelConfs3;

    @ApolloJsonValue("${reco.server.ab.retrieval.channels4:[]}")
    private List<RecommendConfiguration.ChannelConf> channelConfs4;

    @ApolloJsonValue("${reco.server.ab.retrieval.channels5:[]}")
    private List<RecommendConfiguration.ChannelConf> channelConfs5;

    @ApolloJsonValue("${reco.server.ab.retrieval.channels6:[]}")
    private List<RecommendConfiguration.ChannelConf> channelConfs6;

    @ApolloJsonValue("${reco.server.ab.retrieval.channels7:[]}")
    private List<RecommendConfiguration.ChannelConf> channelConfs7;

    @ApolloJsonValue("${reco.server.ab.retrieval.channels8:[]}")
    private List<RecommendConfiguration.ChannelConf> channelConfs8;

    @ApolloJsonValue("${reco.server.ab.retrieval.channels9:[]}")
    private List<RecommendConfiguration.ChannelConf> channelConfs9;

    @ApolloJsonValue("${reco.server.strategy.supportRecomended3:[]}")
    private List<ReWeightStrategy> reWeightStrategies ;

    @ApolloJsonValue("${reco.server.strategy.gogoupcourse:[]}")
    private List<BreakInStrategy> breakInStrategies ;

    @ApolloJsonValue("${reco.server.ab.model.configs:{}}")
    private JSONObject modelConfigs;

    @ApolloJsonValue("${reco.server.ab.incrementalTrain.configs:{}}")
    private JSONObject modelConfigs2;

    @Value("${reco.server.retrieval.each.default:10}")
    private int default_each_retrieval_num;

    @Value("${reco.server.rank.default:300}")
    private int DEFAULT_RANK_NUM_AB;

    @ApolloJsonValue("${reco.server.strategy.restrictions:[]}")
    private List<ArticleStrategy> restrictStrategies;
    @ApolloJsonValue("${reco.server.strategy.restrictions1:[]}")
    private List<RestrictStrategy> restrictStrategies1;
    @ApolloJsonValue("${reco.server.strategy.restrictions2:[]}")
    private List<RestrictStrategy> restrictStrategies2;

    @ApolloJsonValue("${reco.server.retrieval.trigger1:{}}")
    private RecommendConfiguration.TriggerConf triggerConf1;

    @ApolloJsonValue("${reco.server.retrieval.trigger2:{}}")
    private RecommendConfiguration.TriggerConf triggerConf2;

    @ApolloJsonValue("${reco.server.retrieval.trigger3:{}}")
    private RecommendConfiguration.TriggerConf triggerConf3;
}
