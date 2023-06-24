// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: reco_service.proto

package com.plato.recoserver.grpc.service;

/**
 * Protobuf enum {@code com.plato.recoserver.grpc.service.Platform}
 */
public enum Platform
    implements com.google.protobuf.ProtocolMessageEnum {
  /**
   * <code>PLATFORM_UNKNOWN = 0;</code>
   */
  PLATFORM_UNKNOWN(0),
  /**
   * <code>PLATFORM_IOS = 1;</code>
   */
  PLATFORM_IOS(1),
  /**
   * <code>PLATFORM_ANDROID = 2;</code>
   */
  PLATFORM_ANDROID(2),
  /**
   * <code>PLATFORM_PCW = 3;</code>
   */
  PLATFORM_PCW(3),
  UNRECOGNIZED(-1),
  ;

  /**
   * <code>PLATFORM_UNKNOWN = 0;</code>
   */
  public static final int PLATFORM_UNKNOWN_VALUE = 0;
  /**
   * <code>PLATFORM_IOS = 1;</code>
   */
  public static final int PLATFORM_IOS_VALUE = 1;
  /**
   * <code>PLATFORM_ANDROID = 2;</code>
   */
  public static final int PLATFORM_ANDROID_VALUE = 2;
  /**
   * <code>PLATFORM_PCW = 3;</code>
   */
  public static final int PLATFORM_PCW_VALUE = 3;


  public final int getNumber() {
    if (this == UNRECOGNIZED) {
      throw new java.lang.IllegalArgumentException(
          "Can't get the number of an unknown enum value.");
    }
    return value;
  }

  /**
   * @param value The numeric wire value of the corresponding enum entry.
   * @return The enum associated with the given numeric wire value.
   * @deprecated Use {@link #forNumber(int)} instead.
   */
  @java.lang.Deprecated
  public static Platform valueOf(int value) {
    return forNumber(value);
  }

  /**
   * @param value The numeric wire value of the corresponding enum entry.
   * @return The enum associated with the given numeric wire value.
   */
  public static Platform forNumber(int value) {
    switch (value) {
      case 0: return PLATFORM_UNKNOWN;
      case 1: return PLATFORM_IOS;
      case 2: return PLATFORM_ANDROID;
      case 3: return PLATFORM_PCW;
      default: return null;
    }
  }

  public static com.google.protobuf.Internal.EnumLiteMap<Platform>
      internalGetValueMap() {
    return internalValueMap;
  }
  private static final com.google.protobuf.Internal.EnumLiteMap<
      Platform> internalValueMap =
        new com.google.protobuf.Internal.EnumLiteMap<Platform>() {
          public Platform findValueByNumber(int number) {
            return Platform.forNumber(number);
          }
        };

  public final com.google.protobuf.Descriptors.EnumValueDescriptor
      getValueDescriptor() {
    if (this == UNRECOGNIZED) {
      throw new java.lang.IllegalStateException(
          "Can't get the descriptor of an unrecognized enum value.");
    }
    return getDescriptor().getValues().get(ordinal());
  }
  public final com.google.protobuf.Descriptors.EnumDescriptor
      getDescriptorForType() {
    return getDescriptor();
  }
  public static final com.google.protobuf.Descriptors.EnumDescriptor
      getDescriptor() {
    return com.plato.recoserver.grpc.service.RecoServer.getDescriptor().getEnumTypes().get(0);
  }

  private static final Platform[] VALUES = values();

  public static Platform valueOf(
      com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
    if (desc.getType() != getDescriptor()) {
      throw new java.lang.IllegalArgumentException(
        "EnumValueDescriptor is not for this type.");
    }
    if (desc.getIndex() == -1) {
      return UNRECOGNIZED;
    }
    return VALUES[desc.getIndex()];
  }

  private final int value;

  private Platform(int value) {
    this.value = value;
  }

  // @@protoc_insertion_point(enum_scope:com.plato.recoserver.grpc.service.Platform)
}

