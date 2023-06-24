// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: reco_service.proto

package com.plato.recoserver.grpc.service;

public interface RecoRequestOrBuilder extends
    // @@protoc_insertion_point(interface_extends:com.plato.recoserver.grpc.service.RecoRequest)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <pre>
   *本次请求的唯一标识，保证每次请求唯一
   * </pre>
   *
   * <code>string request_id = 1;</code>
   * @return The requestId.
   */
  java.lang.String getRequestId();
  /**
   * <pre>
   *本次请求的唯一标识，保证每次请求唯一
   * </pre>
   *
   * <code>string request_id = 1;</code>
   * @return The bytes for requestId.
   */
  com.google.protobuf.ByteString
      getRequestIdBytes();

  /**
   * <pre>
   *请求用户的设备id
   * </pre>
   *
   * <code>string device_id = 2;</code>
   * @return The deviceId.
   */
  java.lang.String getDeviceId();
  /**
   * <pre>
   *请求用户的设备id
   * </pre>
   *
   * <code>string device_id = 2;</code>
   * @return The bytes for deviceId.
   */
  com.google.protobuf.ByteString
      getDeviceIdBytes();

  /**
   * <pre>
   *登陆用户的user_id
   * </pre>
   *
   * <code>int64 user_id = 3;</code>
   * @return The userId.
   */
  long getUserId();

  /**
   * <pre>
   *用户的会员状态
   * </pre>
   *
   * <code>string vip_status = 4;</code>
   * @return The vipStatus.
   */
  java.lang.String getVipStatus();
  /**
   * <pre>
   *用户的会员状态
   * </pre>
   *
   * <code>string vip_status = 4;</code>
   * @return The bytes for vipStatus.
   */
  com.google.protobuf.ByteString
      getVipStatusBytes();

  /**
   * <pre>
   *推荐场景
   * </pre>
   *
   * <code>string rec_scene = 5;</code>
   * @return The recScene.
   */
  java.lang.String getRecScene();
  /**
   * <pre>
   *推荐场景
   * </pre>
   *
   * <code>string rec_scene = 5;</code>
   * @return The bytes for recScene.
   */
  com.google.protobuf.ByteString
      getRecSceneBytes();

  /**
   * <pre>
   *用户连续请求的唯一标识
   * </pre>
   *
   * <code>string session_id = 6;</code>
   * @return The sessionId.
   */
  java.lang.String getSessionId();
  /**
   * <pre>
   *用户连续请求的唯一标识
   * </pre>
   *
   * <code>string session_id = 6;</code>
   * @return The bytes for sessionId.
   */
  com.google.protobuf.ByteString
      getSessionIdBytes();

  /**
   * <pre>
   *本次请求需要返回的数量
   * </pre>
   *
   * <code>int32 num = 7;</code>
   * @return The num.
   */
  int getNum();

  /**
   * <pre>
   *用户请求的页面标识
   * </pre>
   *
   * <code>string page_id = 8;</code>
   * @return The pageId.
   */
  java.lang.String getPageId();
  /**
   * <pre>
   *用户请求的页面标识
   * </pre>
   *
   * <code>string page_id = 8;</code>
   * @return The bytes for pageId.
   */
  com.google.protobuf.ByteString
      getPageIdBytes();

  /**
   * <pre>
   *用户当前请求所在页的页码
   * </pre>
   *
   * <code>int32 page_num = 9;</code>
   * @return The pageNum.
   */
  int getPageNum();

  /**
   * <pre>
   *请求用户的平台类型
   * </pre>
   *
   * <code>.com.plato.recoserver.grpc.service.Platform platform = 10;</code>
   * @return The enum numeric value on the wire for platform.
   */
  int getPlatformValue();
  /**
   * <pre>
   *请求用户的平台类型
   * </pre>
   *
   * <code>.com.plato.recoserver.grpc.service.Platform platform = 10;</code>
   * @return The platform.
   */
  com.plato.recoserver.grpc.service.Platform getPlatform();

  /**
   * <pre>
   *请求用户的设备操作系统
   * </pre>
   *
   * <code>string os = 11;</code>
   * @return The os.
   */
  java.lang.String getOs();
  /**
   * <pre>
   *请求用户的设备操作系统
   * </pre>
   *
   * <code>string os = 11;</code>
   * @return The bytes for os.
   */
  com.google.protobuf.ByteString
      getOsBytes();

  /**
   * <pre>
   *用户的网络状态
   * </pre>
   *
   * <code>string network = 12;</code>
   * @return The network.
   */
  java.lang.String getNetwork();
  /**
   * <pre>
   *用户的网络状态
   * </pre>
   *
   * <code>string network = 12;</code>
   * @return The bytes for network.
   */
  com.google.protobuf.ByteString
      getNetworkBytes();

  /**
   * <pre>
   *用户地理位置，经纬度表示用英文逗号分隔
   * </pre>
   *
   * <code>string gps = 13;</code>
   * @return The gps.
   */
  java.lang.String getGps();
  /**
   * <pre>
   *用户地理位置，经纬度表示用英文逗号分隔
   * </pre>
   *
   * <code>string gps = 13;</code>
   * @return The bytes for gps.
   */
  com.google.protobuf.ByteString
      getGpsBytes();

  /**
   * <pre>
   *请求用户的ip地址
   * </pre>
   *
   * <code>string ip = 14;</code>
   * @return The ip.
   */
  java.lang.String getIp();
  /**
   * <pre>
   *请求用户的ip地址
   * </pre>
   *
   * <code>string ip = 14;</code>
   * @return The bytes for ip.
   */
  com.google.protobuf.ByteString
      getIpBytes();

  /**
   * <pre>
   *本次请求需要过滤的内容
   * </pre>
   *
   * <code>repeated .com.plato.recoserver.grpc.service.RecoItem filters = 15;</code>
   */
  java.util.List<com.plato.recoserver.grpc.service.RecoItem> 
      getFiltersList();
  /**
   * <pre>
   *本次请求需要过滤的内容
   * </pre>
   *
   * <code>repeated .com.plato.recoserver.grpc.service.RecoItem filters = 15;</code>
   */
  com.plato.recoserver.grpc.service.RecoItem getFilters(int index);
  /**
   * <pre>
   *本次请求需要过滤的内容
   * </pre>
   *
   * <code>repeated .com.plato.recoserver.grpc.service.RecoItem filters = 15;</code>
   */
  int getFiltersCount();
  /**
   * <pre>
   *本次请求需要过滤的内容
   * </pre>
   *
   * <code>repeated .com.plato.recoserver.grpc.service.RecoItem filters = 15;</code>
   */
  java.util.List<? extends com.plato.recoserver.grpc.service.RecoItemOrBuilder> 
      getFiltersOrBuilderList();
  /**
   * <pre>
   *本次请求需要过滤的内容
   * </pre>
   *
   * <code>repeated .com.plato.recoserver.grpc.service.RecoItem filters = 15;</code>
   */
  com.plato.recoserver.grpc.service.RecoItemOrBuilder getFiltersOrBuilder(
      int index);
}
