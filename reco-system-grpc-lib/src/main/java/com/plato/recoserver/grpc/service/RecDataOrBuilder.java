// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: reco_service.proto

package com.plato.recoserver.grpc.service;

public interface RecDataOrBuilder extends
    // @@protoc_insertion_point(interface_extends:com.plato.recoserver.grpc.service.RecData)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>repeated .com.plato.recoserver.grpc.service.RecoItem rec_item = 1;</code>
   */
  java.util.List<com.plato.recoserver.grpc.service.RecoItem> 
      getRecItemList();
  /**
   * <code>repeated .com.plato.recoserver.grpc.service.RecoItem rec_item = 1;</code>
   */
  com.plato.recoserver.grpc.service.RecoItem getRecItem(int index);
  /**
   * <code>repeated .com.plato.recoserver.grpc.service.RecoItem rec_item = 1;</code>
   */
  int getRecItemCount();
  /**
   * <code>repeated .com.plato.recoserver.grpc.service.RecoItem rec_item = 1;</code>
   */
  java.util.List<? extends com.plato.recoserver.grpc.service.RecoItemOrBuilder> 
      getRecItemOrBuilderList();
  /**
   * <code>repeated .com.plato.recoserver.grpc.service.RecoItem rec_item = 1;</code>
   */
  com.plato.recoserver.grpc.service.RecoItemOrBuilder getRecItemOrBuilder(
      int index);
}
