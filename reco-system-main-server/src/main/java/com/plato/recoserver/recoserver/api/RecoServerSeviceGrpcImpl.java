package com.plato.recoserver.recoserver.api;

import com.plato.recoserver.grpc.service.*;
import com.plato.recoserver.recoserver.common.CandidateItem;
import com.plato.recoserver.recoserver.common.Item;
import com.plato.recoserver.recoserver.core.context.RecommendContext;
import com.plato.recoserver.recoserver.core.service.RecommendService;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author lishuguang
 * @date 2022-03-03
 */
@Slf4j
@GrpcService
public class RecoServerSeviceGrpcImpl extends RecoServerServiceGrpc.RecoServerServiceImplBase {
    @Autowired
    private RecommendService recommendService;

    @Override
    public void getRecoResult(RecoRequest request, StreamObserver<RecoResponse> responseObserver) {
        RecommendContext recommendContext = new RecommendContext(request);
        List<Item> recList = recommendService.recommend(recommendContext);

        RecoResponse response = RecoResponse.newBuilder()
                .setCode("A0000")
                .addRecData(buildRecData(recommendContext, recList))
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    private RecData buildRecData(RecommendContext context, List<Item> itemList) {
        if (CollectionUtils.isEmpty(itemList)) {
            return RecData.getDefaultInstance();
        }
        RecData.Builder recDataBuilder = RecData.newBuilder();
        String extendInfo = context.getAbTestProperty().getAbGroups();
        itemList.stream()
                .filter(e -> e instanceof CandidateItem)
                .map(e -> (CandidateItem)e)
                .map(e -> convert(extendInfo, e))
                .forEach(recDataBuilder::addRecItem);
        return recDataBuilder.build();
    }

    private RecoItem convert(String abBuckets, CandidateItem item) {
        StringBuilder tracking = new StringBuilder();
        tracking
        .append("abBuckets=").append(abBuckets);
        return RecoItem.newBuilder()
                .setItemId(item.getId())
                .setType(RecoItem.ItemType.forNumber(item.type()))
                .setItemTracking(tracking.toString())
                .build();
    }
}
