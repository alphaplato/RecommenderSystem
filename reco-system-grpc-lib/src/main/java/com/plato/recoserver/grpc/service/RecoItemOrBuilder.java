// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: reco_service.proto

package com.plato.recoserver.grpc.service;

public interface RecoItemOrBuilder extends
    // @@protoc_insertion_point(interface_extends:com.plato.recoserver.grpc.service.RecoItem)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <pre>
   *item的id
   * </pre>
   *
   * <code>int64 item_id = 1;</code>
   * @return The itemId.
   */
  long getItemId();

  /**
   * <pre>
   *item的类型
   * </pre>
   *
   * <code>.com.plato.recoserver.grpc.service.RecoItem.ItemType type = 2;</code>
   * @return The enum numeric value on the wire for type.
   */
  int getTypeValue();
  /**
   * <pre>
   *item的类型
   * </pre>
   *
   * <code>.com.plato.recoserver.grpc.service.RecoItem.ItemType type = 2;</code>
   * @return The type.
   */
  com.plato.recoserver.grpc.service.RecoItem.ItemType getType();

  /**
   * <code>string item_tracking = 3;</code>
   * @return The itemTracking.
   */
  java.lang.String getItemTracking();
  /**
   * <code>string item_tracking = 3;</code>
   * @return The bytes for itemTracking.
   */
  com.google.protobuf.ByteString
      getItemTrackingBytes();

  /**
   * <code>string item_id2 = 4;</code>
   * @return The itemId2.
   */
  java.lang.String getItemId2();
  /**
   * <code>string item_id2 = 4;</code>
   * @return The bytes for itemId2.
   */
  com.google.protobuf.ByteString
      getItemId2Bytes();
}