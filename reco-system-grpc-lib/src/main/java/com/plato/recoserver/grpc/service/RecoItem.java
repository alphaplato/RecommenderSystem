// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: reco_service.proto

package com.plato.recoserver.grpc.service;

/**
 * Protobuf type {@code com.plato.recoserver.grpc.service.RecoItem}
 */
public final class RecoItem extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:com.plato.recoserver.grpc.service.RecoItem)
    RecoItemOrBuilder {
private static final long serialVersionUID = 0L;
  // Use RecoItem.newBuilder() to construct.
  private RecoItem(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private RecoItem() {
    type_ = 0;
    itemTracking_ = "";
    itemId2_ = "";
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new RecoItem();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private RecoItem(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    this();
    if (extensionRegistry == null) {
      throw new java.lang.NullPointerException();
    }
    com.google.protobuf.UnknownFieldSet.Builder unknownFields =
        com.google.protobuf.UnknownFieldSet.newBuilder();
    try {
      boolean done = false;
      while (!done) {
        int tag = input.readTag();
        switch (tag) {
          case 0:
            done = true;
            break;
          case 8: {

            itemId_ = input.readInt64();
            break;
          }
          case 16: {
            int rawValue = input.readEnum();

            type_ = rawValue;
            break;
          }
          case 26: {
            java.lang.String s = input.readStringRequireUtf8();

            itemTracking_ = s;
            break;
          }
          case 34: {
            java.lang.String s = input.readStringRequireUtf8();

            itemId2_ = s;
            break;
          }
          default: {
            if (!parseUnknownField(
                input, unknownFields, extensionRegistry, tag)) {
              done = true;
            }
            break;
          }
        }
      }
    } catch (com.google.protobuf.InvalidProtocolBufferException e) {
      throw e.setUnfinishedMessage(this);
    } catch (java.io.IOException e) {
      throw new com.google.protobuf.InvalidProtocolBufferException(
          e).setUnfinishedMessage(this);
    } finally {
      this.unknownFields = unknownFields.build();
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return com.plato.recoserver.grpc.service.RecoServer.internal_static_com_plato_recoserver_grpc_service_RecoItem_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.plato.recoserver.grpc.service.RecoServer.internal_static_com_plato_recoserver_grpc_service_RecoItem_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.plato.recoserver.grpc.service.RecoItem.class, com.plato.recoserver.grpc.service.RecoItem.Builder.class);
  }

  /**
   * Protobuf enum {@code com.plato.recoserver.grpc.service.RecoItem.ItemType}
   */
  public enum ItemType
      implements com.google.protobuf.ProtocolMessageEnum {
    /**
     * <code>UNKNOWN = 0;</code>
     */
    UNKNOWN(0),
    /**
     * <code>PRODUCT = 1;</code>
     */
    PRODUCT(1),
    /**
     * <code>ARTICLE = 2;</code>
     */
    ARTICLE(2),
    /**
     * <code>COLLECTION = 3;</code>
     */
    COLLECTION(3),
    /**
     * <code>FAVORITE = 4;</code>
     */
    FAVORITE(4),
    /**
     * <code>HELLORFIMAGE = 5;</code>
     */
    HELLORFIMAGE(5),
    /**
     * <code>HELLORfLOCAlIMAGE = 6;</code>
     */
    HELLORfLOCAlIMAGE(6),
    /**
     * <code>HELLORFVIDEO = 7;</code>
     */
    HELLORFVIDEO(7),
    /**
     * <code>HELLORFIMAGESHUTER = 8;</code>
     */
    HELLORFIMAGESHUTER(8),
    /**
     * <code>HELLORFALBUMSHUTER = 9;</code>
     */
    HELLORFALBUMSHUTER(9),
    /**
     * <code>HELLORFVIDEOSHUTER = 10;</code>
     */
    HELLORFVIDEOSHUTER(10),
    /**
     * <code>HELLORFALBUMS = 17;</code>
     */
    HELLORFALBUMS(17),
    /**
     * <code>LIVECOURSE = 20;</code>
     */
    LIVECOURSE(20),
    /**
     * <code>VIDEOCOURSE = 21;</code>
     */
    VIDEOCOURSE(21),
    /**
     * <code>MEMBERVIDEO = 22;</code>
     */
    MEMBERVIDEO(22),
    UNRECOGNIZED(-1),
    ;

    /**
     * <code>UNKNOWN = 0;</code>
     */
    public static final int UNKNOWN_VALUE = 0;
    /**
     * <code>PRODUCT = 1;</code>
     */
    public static final int PRODUCT_VALUE = 1;
    /**
     * <code>ARTICLE = 2;</code>
     */
    public static final int ARTICLE_VALUE = 2;
    /**
     * <code>COLLECTION = 3;</code>
     */
    public static final int COLLECTION_VALUE = 3;
    /**
     * <code>FAVORITE = 4;</code>
     */
    public static final int FAVORITE_VALUE = 4;
    /**
     * <code>HELLORFIMAGE = 5;</code>
     */
    public static final int HELLORFIMAGE_VALUE = 5;
    /**
     * <code>HELLORfLOCAlIMAGE = 6;</code>
     */
    public static final int HELLORfLOCAlIMAGE_VALUE = 6;
    /**
     * <code>HELLORFVIDEO = 7;</code>
     */
    public static final int HELLORFVIDEO_VALUE = 7;
    /**
     * <code>HELLORFIMAGESHUTER = 8;</code>
     */
    public static final int HELLORFIMAGESHUTER_VALUE = 8;
    /**
     * <code>HELLORFALBUMSHUTER = 9;</code>
     */
    public static final int HELLORFALBUMSHUTER_VALUE = 9;
    /**
     * <code>HELLORFVIDEOSHUTER = 10;</code>
     */
    public static final int HELLORFVIDEOSHUTER_VALUE = 10;
    /**
     * <code>HELLORFALBUMS = 17;</code>
     */
    public static final int HELLORFALBUMS_VALUE = 17;
    /**
     * <code>LIVECOURSE = 20;</code>
     */
    public static final int LIVECOURSE_VALUE = 20;
    /**
     * <code>VIDEOCOURSE = 21;</code>
     */
    public static final int VIDEOCOURSE_VALUE = 21;
    /**
     * <code>MEMBERVIDEO = 22;</code>
     */
    public static final int MEMBERVIDEO_VALUE = 22;


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
    public static ItemType valueOf(int value) {
      return forNumber(value);
    }

    /**
     * @param value The numeric wire value of the corresponding enum entry.
     * @return The enum associated with the given numeric wire value.
     */
    public static ItemType forNumber(int value) {
      switch (value) {
        case 0: return UNKNOWN;
        case 1: return PRODUCT;
        case 2: return ARTICLE;
        case 3: return COLLECTION;
        case 4: return FAVORITE;
        case 5: return HELLORFIMAGE;
        case 6: return HELLORfLOCAlIMAGE;
        case 7: return HELLORFVIDEO;
        case 8: return HELLORFIMAGESHUTER;
        case 9: return HELLORFALBUMSHUTER;
        case 10: return HELLORFVIDEOSHUTER;
        case 17: return HELLORFALBUMS;
        case 20: return LIVECOURSE;
        case 21: return VIDEOCOURSE;
        case 22: return MEMBERVIDEO;
        default: return null;
      }
    }

    public static com.google.protobuf.Internal.EnumLiteMap<ItemType>
        internalGetValueMap() {
      return internalValueMap;
    }
    private static final com.google.protobuf.Internal.EnumLiteMap<
        ItemType> internalValueMap =
          new com.google.protobuf.Internal.EnumLiteMap<ItemType>() {
            public ItemType findValueByNumber(int number) {
              return ItemType.forNumber(number);
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
      return com.plato.recoserver.grpc.service.RecoItem.getDescriptor().getEnumTypes().get(0);
    }

    private static final ItemType[] VALUES = values();

    public static ItemType valueOf(
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

    private ItemType(int value) {
      this.value = value;
    }

    // @@protoc_insertion_point(enum_scope:com.plato.recoserver.grpc.service.RecoItem.ItemType)
  }

  public static final int ITEM_ID_FIELD_NUMBER = 1;
  private long itemId_;
  /**
   * <pre>
   *item的id
   * </pre>
   *
   * <code>int64 item_id = 1;</code>
   * @return The itemId.
   */
  @java.lang.Override
  public long getItemId() {
    return itemId_;
  }

  public static final int TYPE_FIELD_NUMBER = 2;
  private int type_;
  /**
   * <pre>
   *item的类型
   * </pre>
   *
   * <code>.com.plato.recoserver.grpc.service.RecoItem.ItemType type = 2;</code>
   * @return The enum numeric value on the wire for type.
   */
  @java.lang.Override public int getTypeValue() {
    return type_;
  }
  /**
   * <pre>
   *item的类型
   * </pre>
   *
   * <code>.com.plato.recoserver.grpc.service.RecoItem.ItemType type = 2;</code>
   * @return The type.
   */
  @java.lang.Override public com.plato.recoserver.grpc.service.RecoItem.ItemType getType() {
    @SuppressWarnings("deprecation")
    com.plato.recoserver.grpc.service.RecoItem.ItemType result = com.plato.recoserver.grpc.service.RecoItem.ItemType.valueOf(type_);
    return result == null ? com.plato.recoserver.grpc.service.RecoItem.ItemType.UNRECOGNIZED : result;
  }

  public static final int ITEM_TRACKING_FIELD_NUMBER = 3;
  private volatile java.lang.Object itemTracking_;
  /**
   * <code>string item_tracking = 3;</code>
   * @return The itemTracking.
   */
  @java.lang.Override
  public java.lang.String getItemTracking() {
    java.lang.Object ref = itemTracking_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      itemTracking_ = s;
      return s;
    }
  }
  /**
   * <code>string item_tracking = 3;</code>
   * @return The bytes for itemTracking.
   */
  @java.lang.Override
  public com.google.protobuf.ByteString
      getItemTrackingBytes() {
    java.lang.Object ref = itemTracking_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      itemTracking_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int ITEM_ID2_FIELD_NUMBER = 4;
  private volatile java.lang.Object itemId2_;
  /**
   * <code>string item_id2 = 4;</code>
   * @return The itemId2.
   */
  @java.lang.Override
  public java.lang.String getItemId2() {
    java.lang.Object ref = itemId2_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      itemId2_ = s;
      return s;
    }
  }
  /**
   * <code>string item_id2 = 4;</code>
   * @return The bytes for itemId2.
   */
  @java.lang.Override
  public com.google.protobuf.ByteString
      getItemId2Bytes() {
    java.lang.Object ref = itemId2_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      itemId2_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  private byte memoizedIsInitialized = -1;
  @java.lang.Override
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  @java.lang.Override
  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    if (itemId_ != 0L) {
      output.writeInt64(1, itemId_);
    }
    if (type_ != com.plato.recoserver.grpc.service.RecoItem.ItemType.UNKNOWN.getNumber()) {
      output.writeEnum(2, type_);
    }
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(itemTracking_)) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 3, itemTracking_);
    }
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(itemId2_)) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 4, itemId2_);
    }
    unknownFields.writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (itemId_ != 0L) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt64Size(1, itemId_);
    }
    if (type_ != com.plato.recoserver.grpc.service.RecoItem.ItemType.UNKNOWN.getNumber()) {
      size += com.google.protobuf.CodedOutputStream
        .computeEnumSize(2, type_);
    }
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(itemTracking_)) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(3, itemTracking_);
    }
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(itemId2_)) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(4, itemId2_);
    }
    size += unknownFields.getSerializedSize();
    memoizedSize = size;
    return size;
  }

  @java.lang.Override
  public boolean equals(final java.lang.Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof com.plato.recoserver.grpc.service.RecoItem)) {
      return super.equals(obj);
    }
    com.plato.recoserver.grpc.service.RecoItem other = (com.plato.recoserver.grpc.service.RecoItem) obj;

    if (getItemId()
        != other.getItemId()) return false;
    if (type_ != other.type_) return false;
    if (!getItemTracking()
        .equals(other.getItemTracking())) return false;
    if (!getItemId2()
        .equals(other.getItemId2())) return false;
    if (!unknownFields.equals(other.unknownFields)) return false;
    return true;
  }

  @java.lang.Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    hash = (37 * hash) + ITEM_ID_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
        getItemId());
    hash = (37 * hash) + TYPE_FIELD_NUMBER;
    hash = (53 * hash) + type_;
    hash = (37 * hash) + ITEM_TRACKING_FIELD_NUMBER;
    hash = (53 * hash) + getItemTracking().hashCode();
    hash = (37 * hash) + ITEM_ID2_FIELD_NUMBER;
    hash = (53 * hash) + getItemId2().hashCode();
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.plato.recoserver.grpc.service.RecoItem parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.plato.recoserver.grpc.service.RecoItem parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.plato.recoserver.grpc.service.RecoItem parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.plato.recoserver.grpc.service.RecoItem parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.plato.recoserver.grpc.service.RecoItem parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.plato.recoserver.grpc.service.RecoItem parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.plato.recoserver.grpc.service.RecoItem parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.plato.recoserver.grpc.service.RecoItem parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.plato.recoserver.grpc.service.RecoItem parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static com.plato.recoserver.grpc.service.RecoItem parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.plato.recoserver.grpc.service.RecoItem parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.plato.recoserver.grpc.service.RecoItem parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  @java.lang.Override
  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(com.plato.recoserver.grpc.service.RecoItem prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  @java.lang.Override
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @java.lang.Override
  protected Builder newBuilderForType(
      com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * Protobuf type {@code com.plato.recoserver.grpc.service.RecoItem}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:com.plato.recoserver.grpc.service.RecoItem)
      com.plato.recoserver.grpc.service.RecoItemOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.plato.recoserver.grpc.service.RecoServer.internal_static_com_plato_recoserver_grpc_service_RecoItem_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.plato.recoserver.grpc.service.RecoServer.internal_static_com_plato_recoserver_grpc_service_RecoItem_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.plato.recoserver.grpc.service.RecoItem.class, com.plato.recoserver.grpc.service.RecoItem.Builder.class);
    }

    // Construct using com.plato.recoserver.grpc.service.RecoItem.newBuilder()
    private Builder() {
      maybeForceBuilderInitialization();
    }

    private Builder(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);
      maybeForceBuilderInitialization();
    }
    private void maybeForceBuilderInitialization() {
      if (com.google.protobuf.GeneratedMessageV3
              .alwaysUseFieldBuilders) {
      }
    }
    @java.lang.Override
    public Builder clear() {
      super.clear();
      itemId_ = 0L;

      type_ = 0;

      itemTracking_ = "";

      itemId2_ = "";

      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return com.plato.recoserver.grpc.service.RecoServer.internal_static_com_plato_recoserver_grpc_service_RecoItem_descriptor;
    }

    @java.lang.Override
    public com.plato.recoserver.grpc.service.RecoItem getDefaultInstanceForType() {
      return com.plato.recoserver.grpc.service.RecoItem.getDefaultInstance();
    }

    @java.lang.Override
    public com.plato.recoserver.grpc.service.RecoItem build() {
      com.plato.recoserver.grpc.service.RecoItem result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public com.plato.recoserver.grpc.service.RecoItem buildPartial() {
      com.plato.recoserver.grpc.service.RecoItem result = new com.plato.recoserver.grpc.service.RecoItem(this);
      result.itemId_ = itemId_;
      result.type_ = type_;
      result.itemTracking_ = itemTracking_;
      result.itemId2_ = itemId2_;
      onBuilt();
      return result;
    }

    @java.lang.Override
    public Builder clone() {
      return super.clone();
    }
    @java.lang.Override
    public Builder setField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return super.setField(field, value);
    }
    @java.lang.Override
    public Builder clearField(
        com.google.protobuf.Descriptors.FieldDescriptor field) {
      return super.clearField(field);
    }
    @java.lang.Override
    public Builder clearOneof(
        com.google.protobuf.Descriptors.OneofDescriptor oneof) {
      return super.clearOneof(oneof);
    }
    @java.lang.Override
    public Builder setRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        int index, java.lang.Object value) {
      return super.setRepeatedField(field, index, value);
    }
    @java.lang.Override
    public Builder addRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return super.addRepeatedField(field, value);
    }
    @java.lang.Override
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof com.plato.recoserver.grpc.service.RecoItem) {
        return mergeFrom((com.plato.recoserver.grpc.service.RecoItem)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.plato.recoserver.grpc.service.RecoItem other) {
      if (other == com.plato.recoserver.grpc.service.RecoItem.getDefaultInstance()) return this;
      if (other.getItemId() != 0L) {
        setItemId(other.getItemId());
      }
      if (other.type_ != 0) {
        setTypeValue(other.getTypeValue());
      }
      if (!other.getItemTracking().isEmpty()) {
        itemTracking_ = other.itemTracking_;
        onChanged();
      }
      if (!other.getItemId2().isEmpty()) {
        itemId2_ = other.itemId2_;
        onChanged();
      }
      this.mergeUnknownFields(other.unknownFields);
      onChanged();
      return this;
    }

    @java.lang.Override
    public final boolean isInitialized() {
      return true;
    }

    @java.lang.Override
    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      com.plato.recoserver.grpc.service.RecoItem parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (com.plato.recoserver.grpc.service.RecoItem) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }

    private long itemId_ ;
    /**
     * <pre>
     *item的id
     * </pre>
     *
     * <code>int64 item_id = 1;</code>
     * @return The itemId.
     */
    @java.lang.Override
    public long getItemId() {
      return itemId_;
    }
    /**
     * <pre>
     *item的id
     * </pre>
     *
     * <code>int64 item_id = 1;</code>
     * @param value The itemId to set.
     * @return This builder for chaining.
     */
    public Builder setItemId(long value) {
      
      itemId_ = value;
      onChanged();
      return this;
    }
    /**
     * <pre>
     *item的id
     * </pre>
     *
     * <code>int64 item_id = 1;</code>
     * @return This builder for chaining.
     */
    public Builder clearItemId() {
      
      itemId_ = 0L;
      onChanged();
      return this;
    }

    private int type_ = 0;
    /**
     * <pre>
     *item的类型
     * </pre>
     *
     * <code>.com.plato.recoserver.grpc.service.RecoItem.ItemType type = 2;</code>
     * @return The enum numeric value on the wire for type.
     */
    @java.lang.Override public int getTypeValue() {
      return type_;
    }
    /**
     * <pre>
     *item的类型
     * </pre>
     *
     * <code>.com.plato.recoserver.grpc.service.RecoItem.ItemType type = 2;</code>
     * @param value The enum numeric value on the wire for type to set.
     * @return This builder for chaining.
     */
    public Builder setTypeValue(int value) {
      
      type_ = value;
      onChanged();
      return this;
    }
    /**
     * <pre>
     *item的类型
     * </pre>
     *
     * <code>.com.plato.recoserver.grpc.service.RecoItem.ItemType type = 2;</code>
     * @return The type.
     */
    @java.lang.Override
    public com.plato.recoserver.grpc.service.RecoItem.ItemType getType() {
      @SuppressWarnings("deprecation")
      com.plato.recoserver.grpc.service.RecoItem.ItemType result = com.plato.recoserver.grpc.service.RecoItem.ItemType.valueOf(type_);
      return result == null ? com.plato.recoserver.grpc.service.RecoItem.ItemType.UNRECOGNIZED : result;
    }
    /**
     * <pre>
     *item的类型
     * </pre>
     *
     * <code>.com.plato.recoserver.grpc.service.RecoItem.ItemType type = 2;</code>
     * @param value The type to set.
     * @return This builder for chaining.
     */
    public Builder setType(com.plato.recoserver.grpc.service.RecoItem.ItemType value) {
      if (value == null) {
        throw new NullPointerException();
      }
      
      type_ = value.getNumber();
      onChanged();
      return this;
    }
    /**
     * <pre>
     *item的类型
     * </pre>
     *
     * <code>.com.plato.recoserver.grpc.service.RecoItem.ItemType type = 2;</code>
     * @return This builder for chaining.
     */
    public Builder clearType() {
      
      type_ = 0;
      onChanged();
      return this;
    }

    private java.lang.Object itemTracking_ = "";
    /**
     * <code>string item_tracking = 3;</code>
     * @return The itemTracking.
     */
    public java.lang.String getItemTracking() {
      java.lang.Object ref = itemTracking_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        itemTracking_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <code>string item_tracking = 3;</code>
     * @return The bytes for itemTracking.
     */
    public com.google.protobuf.ByteString
        getItemTrackingBytes() {
      java.lang.Object ref = itemTracking_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        itemTracking_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string item_tracking = 3;</code>
     * @param value The itemTracking to set.
     * @return This builder for chaining.
     */
    public Builder setItemTracking(
        java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      itemTracking_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>string item_tracking = 3;</code>
     * @return This builder for chaining.
     */
    public Builder clearItemTracking() {
      
      itemTracking_ = getDefaultInstance().getItemTracking();
      onChanged();
      return this;
    }
    /**
     * <code>string item_tracking = 3;</code>
     * @param value The bytes for itemTracking to set.
     * @return This builder for chaining.
     */
    public Builder setItemTrackingBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      itemTracking_ = value;
      onChanged();
      return this;
    }

    private java.lang.Object itemId2_ = "";
    /**
     * <code>string item_id2 = 4;</code>
     * @return The itemId2.
     */
    public java.lang.String getItemId2() {
      java.lang.Object ref = itemId2_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        itemId2_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <code>string item_id2 = 4;</code>
     * @return The bytes for itemId2.
     */
    public com.google.protobuf.ByteString
        getItemId2Bytes() {
      java.lang.Object ref = itemId2_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        itemId2_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string item_id2 = 4;</code>
     * @param value The itemId2 to set.
     * @return This builder for chaining.
     */
    public Builder setItemId2(
        java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      itemId2_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>string item_id2 = 4;</code>
     * @return This builder for chaining.
     */
    public Builder clearItemId2() {
      
      itemId2_ = getDefaultInstance().getItemId2();
      onChanged();
      return this;
    }
    /**
     * <code>string item_id2 = 4;</code>
     * @param value The bytes for itemId2 to set.
     * @return This builder for chaining.
     */
    public Builder setItemId2Bytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      itemId2_ = value;
      onChanged();
      return this;
    }
    @java.lang.Override
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.setUnknownFields(unknownFields);
    }

    @java.lang.Override
    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.mergeUnknownFields(unknownFields);
    }


    // @@protoc_insertion_point(builder_scope:com.plato.recoserver.grpc.service.RecoItem)
  }

  // @@protoc_insertion_point(class_scope:com.plato.recoserver.grpc.service.RecoItem)
  private static final com.plato.recoserver.grpc.service.RecoItem DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new com.plato.recoserver.grpc.service.RecoItem();
  }

  public static com.plato.recoserver.grpc.service.RecoItem getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<RecoItem>
      PARSER = new com.google.protobuf.AbstractParser<RecoItem>() {
    @java.lang.Override
    public RecoItem parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new RecoItem(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<RecoItem> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<RecoItem> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public com.plato.recoserver.grpc.service.RecoItem getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}
