// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: reco_service.proto

package com.plato.recoserver.grpc.service;

/**
 * Protobuf type {@code com.plato.recoserver.grpc.service.RecData}
 */
public final class RecData extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:com.plato.recoserver.grpc.service.RecData)
    RecDataOrBuilder {
private static final long serialVersionUID = 0L;
  // Use RecData.newBuilder() to construct.
  private RecData(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private RecData() {
    recItem_ = java.util.Collections.emptyList();
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new RecData();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private RecData(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    this();
    if (extensionRegistry == null) {
      throw new java.lang.NullPointerException();
    }
    int mutable_bitField0_ = 0;
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
          case 10: {
            if (!((mutable_bitField0_ & 0x00000001) != 0)) {
              recItem_ = new java.util.ArrayList<com.plato.recoserver.grpc.service.RecoItem>();
              mutable_bitField0_ |= 0x00000001;
            }
            recItem_.add(
                input.readMessage(com.plato.recoserver.grpc.service.RecoItem.parser(), extensionRegistry));
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
      if (((mutable_bitField0_ & 0x00000001) != 0)) {
        recItem_ = java.util.Collections.unmodifiableList(recItem_);
      }
      this.unknownFields = unknownFields.build();
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return com.plato.recoserver.grpc.service.RecoServer.internal_static_com_plato_recoserver_grpc_service_RecData_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.plato.recoserver.grpc.service.RecoServer.internal_static_com_plato_recoserver_grpc_service_RecData_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.plato.recoserver.grpc.service.RecData.class, com.plato.recoserver.grpc.service.RecData.Builder.class);
  }

  public static final int REC_ITEM_FIELD_NUMBER = 1;
  private java.util.List<com.plato.recoserver.grpc.service.RecoItem> recItem_;
  /**
   * <code>repeated .com.plato.recoserver.grpc.service.RecoItem rec_item = 1;</code>
   */
  @java.lang.Override
  public java.util.List<com.plato.recoserver.grpc.service.RecoItem> getRecItemList() {
    return recItem_;
  }
  /**
   * <code>repeated .com.plato.recoserver.grpc.service.RecoItem rec_item = 1;</code>
   */
  @java.lang.Override
  public java.util.List<? extends com.plato.recoserver.grpc.service.RecoItemOrBuilder> 
      getRecItemOrBuilderList() {
    return recItem_;
  }
  /**
   * <code>repeated .com.plato.recoserver.grpc.service.RecoItem rec_item = 1;</code>
   */
  @java.lang.Override
  public int getRecItemCount() {
    return recItem_.size();
  }
  /**
   * <code>repeated .com.plato.recoserver.grpc.service.RecoItem rec_item = 1;</code>
   */
  @java.lang.Override
  public com.plato.recoserver.grpc.service.RecoItem getRecItem(int index) {
    return recItem_.get(index);
  }
  /**
   * <code>repeated .com.plato.recoserver.grpc.service.RecoItem rec_item = 1;</code>
   */
  @java.lang.Override
  public com.plato.recoserver.grpc.service.RecoItemOrBuilder getRecItemOrBuilder(
      int index) {
    return recItem_.get(index);
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
    for (int i = 0; i < recItem_.size(); i++) {
      output.writeMessage(1, recItem_.get(i));
    }
    unknownFields.writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    for (int i = 0; i < recItem_.size(); i++) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(1, recItem_.get(i));
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
    if (!(obj instanceof com.plato.recoserver.grpc.service.RecData)) {
      return super.equals(obj);
    }
    com.plato.recoserver.grpc.service.RecData other = (com.plato.recoserver.grpc.service.RecData) obj;

    if (!getRecItemList()
        .equals(other.getRecItemList())) return false;
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
    if (getRecItemCount() > 0) {
      hash = (37 * hash) + REC_ITEM_FIELD_NUMBER;
      hash = (53 * hash) + getRecItemList().hashCode();
    }
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.plato.recoserver.grpc.service.RecData parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.plato.recoserver.grpc.service.RecData parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.plato.recoserver.grpc.service.RecData parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.plato.recoserver.grpc.service.RecData parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.plato.recoserver.grpc.service.RecData parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.plato.recoserver.grpc.service.RecData parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.plato.recoserver.grpc.service.RecData parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.plato.recoserver.grpc.service.RecData parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.plato.recoserver.grpc.service.RecData parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static com.plato.recoserver.grpc.service.RecData parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.plato.recoserver.grpc.service.RecData parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.plato.recoserver.grpc.service.RecData parseFrom(
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
  public static Builder newBuilder(com.plato.recoserver.grpc.service.RecData prototype) {
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
   * Protobuf type {@code com.plato.recoserver.grpc.service.RecData}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:com.plato.recoserver.grpc.service.RecData)
      com.plato.recoserver.grpc.service.RecDataOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.plato.recoserver.grpc.service.RecoServer.internal_static_com_plato_recoserver_grpc_service_RecData_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.plato.recoserver.grpc.service.RecoServer.internal_static_com_plato_recoserver_grpc_service_RecData_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.plato.recoserver.grpc.service.RecData.class, com.plato.recoserver.grpc.service.RecData.Builder.class);
    }

    // Construct using com.plato.recoserver.grpc.service.RecData.newBuilder()
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
        getRecItemFieldBuilder();
      }
    }
    @java.lang.Override
    public Builder clear() {
      super.clear();
      if (recItemBuilder_ == null) {
        recItem_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000001);
      } else {
        recItemBuilder_.clear();
      }
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return com.plato.recoserver.grpc.service.RecoServer.internal_static_com_plato_recoserver_grpc_service_RecData_descriptor;
    }

    @java.lang.Override
    public com.plato.recoserver.grpc.service.RecData getDefaultInstanceForType() {
      return com.plato.recoserver.grpc.service.RecData.getDefaultInstance();
    }

    @java.lang.Override
    public com.plato.recoserver.grpc.service.RecData build() {
      com.plato.recoserver.grpc.service.RecData result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public com.plato.recoserver.grpc.service.RecData buildPartial() {
      com.plato.recoserver.grpc.service.RecData result = new com.plato.recoserver.grpc.service.RecData(this);
      int from_bitField0_ = bitField0_;
      if (recItemBuilder_ == null) {
        if (((bitField0_ & 0x00000001) != 0)) {
          recItem_ = java.util.Collections.unmodifiableList(recItem_);
          bitField0_ = (bitField0_ & ~0x00000001);
        }
        result.recItem_ = recItem_;
      } else {
        result.recItem_ = recItemBuilder_.build();
      }
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
      if (other instanceof com.plato.recoserver.grpc.service.RecData) {
        return mergeFrom((com.plato.recoserver.grpc.service.RecData)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.plato.recoserver.grpc.service.RecData other) {
      if (other == com.plato.recoserver.grpc.service.RecData.getDefaultInstance()) return this;
      if (recItemBuilder_ == null) {
        if (!other.recItem_.isEmpty()) {
          if (recItem_.isEmpty()) {
            recItem_ = other.recItem_;
            bitField0_ = (bitField0_ & ~0x00000001);
          } else {
            ensureRecItemIsMutable();
            recItem_.addAll(other.recItem_);
          }
          onChanged();
        }
      } else {
        if (!other.recItem_.isEmpty()) {
          if (recItemBuilder_.isEmpty()) {
            recItemBuilder_.dispose();
            recItemBuilder_ = null;
            recItem_ = other.recItem_;
            bitField0_ = (bitField0_ & ~0x00000001);
            recItemBuilder_ = 
              com.google.protobuf.GeneratedMessageV3.alwaysUseFieldBuilders ?
                 getRecItemFieldBuilder() : null;
          } else {
            recItemBuilder_.addAllMessages(other.recItem_);
          }
        }
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
      com.plato.recoserver.grpc.service.RecData parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (com.plato.recoserver.grpc.service.RecData) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }
    private int bitField0_;

    private java.util.List<com.plato.recoserver.grpc.service.RecoItem> recItem_ =
      java.util.Collections.emptyList();
    private void ensureRecItemIsMutable() {
      if (!((bitField0_ & 0x00000001) != 0)) {
        recItem_ = new java.util.ArrayList<com.plato.recoserver.grpc.service.RecoItem>(recItem_);
        bitField0_ |= 0x00000001;
       }
    }

    private com.google.protobuf.RepeatedFieldBuilderV3<
        com.plato.recoserver.grpc.service.RecoItem, com.plato.recoserver.grpc.service.RecoItem.Builder, com.plato.recoserver.grpc.service.RecoItemOrBuilder> recItemBuilder_;

    /**
     * <code>repeated .com.plato.recoserver.grpc.service.RecoItem rec_item = 1;</code>
     */
    public java.util.List<com.plato.recoserver.grpc.service.RecoItem> getRecItemList() {
      if (recItemBuilder_ == null) {
        return java.util.Collections.unmodifiableList(recItem_);
      } else {
        return recItemBuilder_.getMessageList();
      }
    }
    /**
     * <code>repeated .com.plato.recoserver.grpc.service.RecoItem rec_item = 1;</code>
     */
    public int getRecItemCount() {
      if (recItemBuilder_ == null) {
        return recItem_.size();
      } else {
        return recItemBuilder_.getCount();
      }
    }
    /**
     * <code>repeated .com.plato.recoserver.grpc.service.RecoItem rec_item = 1;</code>
     */
    public com.plato.recoserver.grpc.service.RecoItem getRecItem(int index) {
      if (recItemBuilder_ == null) {
        return recItem_.get(index);
      } else {
        return recItemBuilder_.getMessage(index);
      }
    }
    /**
     * <code>repeated .com.plato.recoserver.grpc.service.RecoItem rec_item = 1;</code>
     */
    public Builder setRecItem(
        int index, com.plato.recoserver.grpc.service.RecoItem value) {
      if (recItemBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureRecItemIsMutable();
        recItem_.set(index, value);
        onChanged();
      } else {
        recItemBuilder_.setMessage(index, value);
      }
      return this;
    }
    /**
     * <code>repeated .com.plato.recoserver.grpc.service.RecoItem rec_item = 1;</code>
     */
    public Builder setRecItem(
        int index, com.plato.recoserver.grpc.service.RecoItem.Builder builderForValue) {
      if (recItemBuilder_ == null) {
        ensureRecItemIsMutable();
        recItem_.set(index, builderForValue.build());
        onChanged();
      } else {
        recItemBuilder_.setMessage(index, builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .com.plato.recoserver.grpc.service.RecoItem rec_item = 1;</code>
     */
    public Builder addRecItem(com.plato.recoserver.grpc.service.RecoItem value) {
      if (recItemBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureRecItemIsMutable();
        recItem_.add(value);
        onChanged();
      } else {
        recItemBuilder_.addMessage(value);
      }
      return this;
    }
    /**
     * <code>repeated .com.plato.recoserver.grpc.service.RecoItem rec_item = 1;</code>
     */
    public Builder addRecItem(
        int index, com.plato.recoserver.grpc.service.RecoItem value) {
      if (recItemBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureRecItemIsMutable();
        recItem_.add(index, value);
        onChanged();
      } else {
        recItemBuilder_.addMessage(index, value);
      }
      return this;
    }
    /**
     * <code>repeated .com.plato.recoserver.grpc.service.RecoItem rec_item = 1;</code>
     */
    public Builder addRecItem(
        com.plato.recoserver.grpc.service.RecoItem.Builder builderForValue) {
      if (recItemBuilder_ == null) {
        ensureRecItemIsMutable();
        recItem_.add(builderForValue.build());
        onChanged();
      } else {
        recItemBuilder_.addMessage(builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .com.plato.recoserver.grpc.service.RecoItem rec_item = 1;</code>
     */
    public Builder addRecItem(
        int index, com.plato.recoserver.grpc.service.RecoItem.Builder builderForValue) {
      if (recItemBuilder_ == null) {
        ensureRecItemIsMutable();
        recItem_.add(index, builderForValue.build());
        onChanged();
      } else {
        recItemBuilder_.addMessage(index, builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .com.plato.recoserver.grpc.service.RecoItem rec_item = 1;</code>
     */
    public Builder addAllRecItem(
        java.lang.Iterable<? extends com.plato.recoserver.grpc.service.RecoItem> values) {
      if (recItemBuilder_ == null) {
        ensureRecItemIsMutable();
        com.google.protobuf.AbstractMessageLite.Builder.addAll(
            values, recItem_);
        onChanged();
      } else {
        recItemBuilder_.addAllMessages(values);
      }
      return this;
    }
    /**
     * <code>repeated .com.plato.recoserver.grpc.service.RecoItem rec_item = 1;</code>
     */
    public Builder clearRecItem() {
      if (recItemBuilder_ == null) {
        recItem_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000001);
        onChanged();
      } else {
        recItemBuilder_.clear();
      }
      return this;
    }
    /**
     * <code>repeated .com.plato.recoserver.grpc.service.RecoItem rec_item = 1;</code>
     */
    public Builder removeRecItem(int index) {
      if (recItemBuilder_ == null) {
        ensureRecItemIsMutable();
        recItem_.remove(index);
        onChanged();
      } else {
        recItemBuilder_.remove(index);
      }
      return this;
    }
    /**
     * <code>repeated .com.plato.recoserver.grpc.service.RecoItem rec_item = 1;</code>
     */
    public com.plato.recoserver.grpc.service.RecoItem.Builder getRecItemBuilder(
        int index) {
      return getRecItemFieldBuilder().getBuilder(index);
    }
    /**
     * <code>repeated .com.plato.recoserver.grpc.service.RecoItem rec_item = 1;</code>
     */
    public com.plato.recoserver.grpc.service.RecoItemOrBuilder getRecItemOrBuilder(
        int index) {
      if (recItemBuilder_ == null) {
        return recItem_.get(index);  } else {
        return recItemBuilder_.getMessageOrBuilder(index);
      }
    }
    /**
     * <code>repeated .com.plato.recoserver.grpc.service.RecoItem rec_item = 1;</code>
     */
    public java.util.List<? extends com.plato.recoserver.grpc.service.RecoItemOrBuilder> 
         getRecItemOrBuilderList() {
      if (recItemBuilder_ != null) {
        return recItemBuilder_.getMessageOrBuilderList();
      } else {
        return java.util.Collections.unmodifiableList(recItem_);
      }
    }
    /**
     * <code>repeated .com.plato.recoserver.grpc.service.RecoItem rec_item = 1;</code>
     */
    public com.plato.recoserver.grpc.service.RecoItem.Builder addRecItemBuilder() {
      return getRecItemFieldBuilder().addBuilder(
          com.plato.recoserver.grpc.service.RecoItem.getDefaultInstance());
    }
    /**
     * <code>repeated .com.plato.recoserver.grpc.service.RecoItem rec_item = 1;</code>
     */
    public com.plato.recoserver.grpc.service.RecoItem.Builder addRecItemBuilder(
        int index) {
      return getRecItemFieldBuilder().addBuilder(
          index, com.plato.recoserver.grpc.service.RecoItem.getDefaultInstance());
    }
    /**
     * <code>repeated .com.plato.recoserver.grpc.service.RecoItem rec_item = 1;</code>
     */
    public java.util.List<com.plato.recoserver.grpc.service.RecoItem.Builder> 
         getRecItemBuilderList() {
      return getRecItemFieldBuilder().getBuilderList();
    }
    private com.google.protobuf.RepeatedFieldBuilderV3<
        com.plato.recoserver.grpc.service.RecoItem, com.plato.recoserver.grpc.service.RecoItem.Builder, com.plato.recoserver.grpc.service.RecoItemOrBuilder> 
        getRecItemFieldBuilder() {
      if (recItemBuilder_ == null) {
        recItemBuilder_ = new com.google.protobuf.RepeatedFieldBuilderV3<
            com.plato.recoserver.grpc.service.RecoItem, com.plato.recoserver.grpc.service.RecoItem.Builder, com.plato.recoserver.grpc.service.RecoItemOrBuilder>(
                recItem_,
                ((bitField0_ & 0x00000001) != 0),
                getParentForChildren(),
                isClean());
        recItem_ = null;
      }
      return recItemBuilder_;
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


    // @@protoc_insertion_point(builder_scope:com.plato.recoserver.grpc.service.RecData)
  }

  // @@protoc_insertion_point(class_scope:com.plato.recoserver.grpc.service.RecData)
  private static final com.plato.recoserver.grpc.service.RecData DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new com.plato.recoserver.grpc.service.RecData();
  }

  public static com.plato.recoserver.grpc.service.RecData getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<RecData>
      PARSER = new com.google.protobuf.AbstractParser<RecData>() {
    @java.lang.Override
    public RecData parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new RecData(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<RecData> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<RecData> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public com.plato.recoserver.grpc.service.RecData getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

