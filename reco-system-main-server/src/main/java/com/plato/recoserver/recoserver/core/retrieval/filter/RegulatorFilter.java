package com.plato.recoserver.recoserver.core.retrieval.filter;

import com.plato.recoserver.recoserver.core.context.RecommendContext;
//import com.zcool.platform.spread.regulate.grpc.lib.ItemType;
//import com.zcool.platform.spread.regulate.grpc.lib.RegulateRequest;
//import com.zcool.platform.spread.regulate.grpc.lib.RegulateResult;
//import com.zcool.platform.spread.regulate.grpc.lib.SpreadRegulatorServiceGrpc;
import com.plato.recoserver.recoserver.common.CandidateItem;
import com.plato.recoserver.recoserver.common.Item;
import com.plato.recoserver.recoserver.core.retrieval.filter.inf.IFilter;
import com.plato.recoserver.grpc.service.RecoRequest;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lishuguang
 * @date 2022/11/8
 **/
@Service
@Slf4j
public class RegulatorFilter implements IFilter {
    @GrpcClient("spread-regulator")
//    private SpreadRegulatorServiceGrpc.SpreadRegulatorServiceBlockingStub spreadRegulatorServiceBlockingStub;

    /**
     * 过滤函数，管控服务，在最外层过滤
     *
     * @param context
     * @param items
     */
    @Override
    public void filter(RecommendContext context, List<CandidateItem> items) {
        List<Item> itemsInput = new LinkedList<>(items);
//        List<RegulateResult> regulateResponse = getRegulatorList(context, itemsInput);
//        tagFilterType(regulateResponse, items);
    }

//    public void tagFilterType(List<RegulateResult> regulateResponse, List<CandidateItem> items){
//        Set<Item> regulateResults = regulateResponse.stream()
//                .map(e-> new CandidateItem(e.getItem().getId(),e.getItem().getType().getNumber(),0))
//                .collect(Collectors.toCollection(HashSet::new));
//
//        if (!CollectionUtils.isEmpty(regulateResults)) {
//            items.parallelStream().forEach(e->{
//                if(regulateResults.contains(e)){
//                    e.setFilterType(CandidateItem.FilterType.REGULATED);
//                }
//            });
//        }
//    }

//    public List<RegulateResult> getRegulatorList(RecommendContext context, List<Item> items){
//        RecoRequest request = context.getRequest();
//        RegulateRequest.Builder regulateRequestBuilder = RegulateRequest.newBuilder();
//        List<com.zcool.platform.spread.regulate.grpc.lib.Item> regulatorInputList = items.stream()
//                .map(e -> {
//                    try {
//                        return com.zcool.platform.spread.regulate.grpc.lib.Item.newBuilder()
//                                .setId((Long) e.getId())
//                                .setType(ItemType.forNumber(e.type()))
//                                .build();
//                    } catch (NullPointerException ex){
//                        return null;
//                    }
//                }).filter(Objects::nonNull)
//                .collect(Collectors.toCollection(LinkedList::new));
//
//        regulateRequestBuilder.addAllItems(regulatorInputList);
//        regulateRequestBuilder.setSceneCode("community_home_feed");
//        if (request.getUserId() > 0) {
//            regulateRequestBuilder.setLoginId(context.getRequest().getUserId());
//        } else {
//            regulateRequestBuilder.setLoginId(0);
//        }
//        List<RegulateResult> regulateResponse = new LinkedList<>();
//        try {
//            regulateResponse = spreadRegulatorServiceBlockingStub.regulate(regulateRequestBuilder.build()).getRegulateResultList();
//
//        } catch (Exception e) {
//            log.warn("/category:filter/warn_name:items are before regulator/size:{}/device_id:{}/user_id:{}/request_id:{}",
//                    CollectionUtils.size(items), request.getDeviceId(), request.getUserId(), request.getRequestId());
//        }
//        return regulateResponse;
//    }
}
