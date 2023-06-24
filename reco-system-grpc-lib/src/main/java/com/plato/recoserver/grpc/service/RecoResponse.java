// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: reco_service.proto

package com.plato.recoserver.grpc.service;

/**
 * Protobuf type {@code com.plato.recoserver.grpc.service.RecoResponse}
 */
public final class RecoResponse extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:com.plato.recoserver.grpc.service.RecoResponse)
    RecoResponseOrBuilder {
private static final long serialVersionUID = 0L;
  // Use RecoResponse.newBuilder() to construct.
  private RecoResponse(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private RecoResponse() {
    code_ = "";
    message_ = "";
    recData_ = java.util.Collections.emptyList();
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new RecoResponse();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private RecoResponse(
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
            java.lang.String s = input.readStringRequireUtf8();

            code_ = s;
            break;
          }
          case 18: {
            java.lang.String s = input.readStringRequireUtf8();

            message_ = s;
            break;
          }
          case 26: {
            if (!((mutable_bitField0_ & 0x00000001) != 0)) {
              recData_ = new java.util.ArrayList<com.plato.recoserver.grpc.service.RecData>();
              mutable_bitField0_ |= 0x00000001;
            }
            recData_.add(
                input.readMessage(com.plato.recoserver.grpc.service.RecData.parser(), extensionRegistry));
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
        recData_ = java.util.Collections.unmodifiableList(recData_);
      }
      this.unknownFields = unknownFields.build();
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return com.plato.recoserver.grpc.service.RecoServer.internal_static_com_plato_recoserver_grpc_service_RecoResponse_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.plato.recoserver.grpc.service.RecoServer.internal_static_com_plato_recoserver_grpc_service_RecoResponse_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.plato.recoserver.grpc.service.RecoResponse.class, com.plato.recoserver.grpc.service.RecoResponse.Builder.class);
  }

  public static final int CODE_FIELD_NUMBER = 1;
  private volatile java.lang.Object code_;
  /**
   * <code>string code = 1;</code>
   * @return The code.
   */
  @java.lang.Override
  public java.lang.String getCode() {
    java.lang.Object ref = code_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      code_ = s;
      return s;
    }
  }
  /**
   * <code>string code = 1;</code>
   * @return The bytes for code.
   */
  @java.lang.Override
  public com.google.protobuf.ByteString
      getCodeBytes() {
    java.lang.Object ref = code_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      code_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int MESSAGE_FIELD_NUMBER = 2;
  private volatile java.lang.Object message_;
  /**
   * <code>string message = 2;</code>
   * @return The message.
   */
  @java.lang.Override
  public java.lang.String getMessage() {
    java.lang.Object ref = message_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      message_ = s;
      return s;
    }
  }
  /**
   * <code>string message = 2;</code>
   * @return The bytes for message.
   */
  @java.lang.Override
  public com.google.protobuf.ByteString
      getMessageBytes() {
    java.lang.Object ref = message_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      message_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int REC_DATA_FIELD_NUMBER = 3;
  private java.util.List<com.plato.recoserver.grpc.service.RecData> recData_;
  /**
   * <code>repeated .com.plato.recoserver.grpc.service.RecData rec_data = 3;</code>
   */
  @java.lang.Override
  public java.util.List<com.plato.recoserver.grpc.service.RecData> getRecDataList() {
    return recData_;
  }
  /**
   * <code>repeated .com.plato.recoserver.grpc.service.RecData rec_data = 3;</code>
   */
  @java.lang.Override
  public java.util.List<? extends com.plato.recoserver.grpc.service.RecDataOrBuilder> 
      getRecDataOrBuilderList() {
    return recData_;
  }
  /**
   * <code>repeated .com.plato.recoserver.grpc.service.RecData rec_data = 3;</code>
   */
  @java.lang.Override
  public int getRecDataCount() {
    return recData_.size();
  }
  /**
   * <code>repeated .com.plato.recoserver.grpc.service.RecData rec_data = 3;</code>
   */
  @java.lang.Override
  public com.plato.recoserver.grpc.service.RecData getRecData(int index) {
    return recData_.get(index);
  }
  /**
   * <code>repeated .com.plato.recoserver.grpc.service.RecData rec_data = 3;</code>
   */
  @java.lang.Override
  public com.plato.recoserver.grpc.service.RecDataOrBuilder getRecDataOrBuilder(
      int index) {
    return recData_.get(index);
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
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(code_)) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 1, code_);
    }
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(message_)) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 2, message_);
    }
    for (int i = 0; i < recData_.size(); i++) {
      output.writeMessage(3, recData_.get(i));
    }
    unknownFields.writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(code_)) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(1, code_);
    }
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(message_)) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(2, message_);
    }
    for (int i = 0; i < recData_.size(); i++) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(3, recData_.get(i));
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
    if (!(obj instanceof com.plato.recoserver.grpc.service.RecoResponse)) {
      return super.equals(obj);
    }
    com.plato.recoserver.grpc.service.RecoResponse other = (com.plato.recoserver.grpc.service.RecoResponse) obj;

    if (!getCode()
        .equals(other.getCode())) return false;
    if (!getMessage()
        .equals(other.getMessage())) return false;
    if (!getRecDataList()
        .equals(other.getRecDataList())) return false;
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
    hash = (37 * hash) + CODE_FIELD_NUMBER;
    hash = (53 * hash) + getCode().hashCode();
    hash = (37 * hash) + MESSAGE_FIELD_NUMBER;
    hash = (53 * hash) + getMessage().hashCode();
    if (getRecDataCount() > 0) {
      hash = (37 * hash) + REC_DATA_FIELD_NUMBER;
      hash = (53 * hash) + getRecDataList().hashCode();
    }
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.plato.recoserver.grpc.service.RecoResponse parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.plato.recoserver.grpc.service.RecoResponse parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.plato.recoserver.grpc.service.RecoResponse parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.plato.recoserver.grpc.service.RecoResponse parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.plato.recoserver.grpc.service.RecoResponse parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.plato.recoserver.grpc.service.RecoResponse parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.plato.recoserver.grpc.service.RecoResponse parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.plato.recoserver.grpc.service.RecoResponse parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.plato.recoserver.grpc.service.RecoResponse parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static com.plato.recoserver.grpc.service.RecoResponse parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.plato.recoserver.grpc.service.RecoResponse parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.plato.recoserver.grpc.service.RecoResponse parseFrom(
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
  public static Builder newBuilder(com.plato.recoserver.grpc.service.RecoResponse prototype) {
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
   * Protobuf type {@code com.plato.recoserver.grpc.service.RecoResponse}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:com.plato.recoserver.grpc.service.RecoResponse)
      com.plato.recoserver.grpc.service.RecoResponseOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.plato.recoserver.grpc.service.RecoServer.internal_static_com_plato_recoserver_grpc_service_RecoResponse_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.plato.recoserver.grpc.service.RecoServer.internal_static_com_plato_recoserver_grpc_service_RecoResponse_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.plato.recoserver.grpc.service.RecoResponse.class, com.plato.recoserver.grpc.service.RecoResponse.Builder.class);
    }

    // Construct using com.plato.recoserver.grpc.service.RecoResponse.newBuilder()
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
        getRecDataFieldBuilder();
      }
    }
    @java.lang.Override
    public Builder clear() {
      super.clear();
      code_ = "";

      message_ = "";

      if (recDataBuilder_ == null) {
        recData_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000001);
      } else {
        recDataBuilder_.clear();
      }
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return com.plato.recoserver.grpc.service.RecoServer.internal_static_com_plato_recoserver_grpc_service_RecoResponse_descriptor;
    }

    @java.lang.Override
    public com.plato.recoserver.grpc.service.RecoResponse getDefaultInstanceForType() {
      return com.plato.recoserver.grpc.service.RecoResponse.getDefaultInstance();
    }

    @java.lang.Override
    public com.plato.recoserver.grpc.service.RecoResponse build() {
      com.plato.recoserver.grpc.service.RecoResponse result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public com.plato.recoserver.grpc.service.RecoResponse buildPartial() {
      com.plato.recoserver.grpc.service.RecoResponse result = new com.plato.recoserver.grpc.service.RecoResponse(this);
      int from_bitField0_ = bitField0_;
      result.code_ = code_;
      result.message_ = message_;
      if (recDataBuilder_ == null) {
        if (((bitField0_ & 0x00000001) != 0)) {
          recData_ = java.util.Collections.unmodifiableList(recData_);
          bitField0_ = (bitField0_ & ~0x00000001);
        }
        result.recData_ = recData_;
      } else {
        result.recData_ = recDataBuilder_.build();
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
      if (other instanceof com.plato.recoserver.grpc.service.RecoResponse) {
        return mergeFrom((com.plato.recoserver.grpc.service.RecoResponse)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.plato.recoserver.grpc.service.RecoResponse other) {
      if (other == com.plato.recoserver.grpc.service.RecoResponse.getDefaultInstance()) return this;
      if (!other.getCode().isEmpty()) {
        code_ = other.code_;
        onChanged();
      }
      if (!other.getMessage().isEmpty()) {
        message_ = other.message_;
        onChanged();
      }
      if (recDataBuilder_ == null) {
        if (!other.recData_.isEmpty()) {
          if (recData_.isEmpty()) {
            recData_ = other.recData_;
            bitField0_ = (bitField0_ & ~0x00000001);
          } else {
            ensureRecDataIsMutable();
            recData_.addAll(other.recData_);
          }
          onChanged();
        }
      } else {
        if (!other.recData_.isEmpty()) {
          if (recDataBuilder_.isEmpty()) {
            recDataBuilder_.dispose();
            recDataBuilder_ = null;
            recData_ = other.recData_;
            bitField0_ = (bitField0_ & ~0x00000001);
            recDataBuilder_ = 
              com.google.protobuf.GeneratedMessageV3.alwaysUseFieldBuilders ?
                 getRecDataFieldBuilder() : null;
          } else {
            recDataBuilder_.addAllMessages(other.recData_);
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
      com.plato.recoserver.grpc.service.RecoResponse parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (com.plato.recoserver.grpc.service.RecoResponse) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }
    private int bitField0_;

    private java.lang.Object code_ = "";
    /**
     * <code>string code = 1;</code>
     * @return The code.
     */
    public java.lang.String getCode() {
      java.lang.Object ref = code_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        code_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <code>string code = 1;</code>
     * @return The bytes for code.
     */
    public com.google.protobuf.ByteString
        getCodeBytes() {
      java.lang.Object ref = code_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        code_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string code = 1;</code>
     * @param value The code to set.
     * @return This builder for chaining.
     */
    public Builder setCode(
        java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      code_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>string code = 1;</code>
     * @return This builder for chaining.
     */
    public Builder clearCode() {
      
      code_ = getDefaultInstance().getCode();
      onChanged();
      return this;
    }
    /**
     * <code>string code = 1;</code>
     * @param value The bytes for code to set.
     * @return This builder for chaining.
     */
    public Builder setCodeBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      code_ = value;
      onChanged();
      return this;
    }

    private java.lang.Object message_ = "";
    /**
     * <code>string message = 2;</code>
     * @return The message.
     */
    public java.lang.String getMessage() {
      java.lang.Object ref = message_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        message_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <code>string message = 2;</code>
     * @return The bytes for message.
     */
    public com.google.protobuf.ByteString
        getMessageBytes() {
      java.lang.Object ref = message_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        message_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string message = 2;</code>
     * @param value The message to set.
     * @return This builder for chaining.
     */
    public Builder setMessage(
        java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      message_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>string message = 2;</code>
     * @return This builder for chaining.
     */
    public Builder clearMessage() {
      
      message_ = getDefaultInstance().getMessage();
      onChanged();
      return this;
    }
    /**
     * <code>string message = 2;</code>
     * @param value The bytes for message to set.
     * @return This builder for chaining.
     */
    public Builder setMessageBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      message_ = value;
      onChanged();
      return this;
    }

    private java.util.List<com.plato.recoserver.grpc.service.RecData> recData_ =
      java.util.Collections.emptyList();
    private void ensureRecDataIsMutable() {
      if (!((bitField0_ & 0x00000001) != 0)) {
        recData_ = new java.util.ArrayList<com.plato.recoserver.grpc.service.RecData>(recData_);
        bitField0_ |= 0x00000001;
       }
    }

    private com.google.protobuf.RepeatedFieldBuilderV3<
        com.plato.recoserver.grpc.service.RecData, com.plato.recoserver.grpc.service.RecData.Builder, com.plato.recoserver.grpc.service.RecDataOrBuilder> recDataBuilder_;

    /**
     * <code>repeated .com.plato.recoserver.grpc.service.RecData rec_data = 3;</code>
     */
    public java.util.List<com.plato.recoserver.grpc.service.RecData> getRecDataList() {
      if (recDataBuilder_ == null) {
        return java.util.Collections.unmodifiableList(recData_);
      } else {
        return recDataBuilder_.getMessageList();
      }
    }
    /**
     * <code>repeated .com.plato.recoserver.grpc.service.RecData rec_data = 3;</code>
     */
    public int getRecDataCount() {
      if (recDataBuilder_ == null) {
        return recData_.size();
      } else {
        return recDataBuilder_.getCount();
      }
    }
    /**
     * <code>repeated .com.plato.recoserver.grpc.service.RecData rec_data = 3;</code>
     */
    public com.plato.recoserver.grpc.service.RecData getRecData(int index) {
      if (recDataBuilder_ == null) {
        return recData_.get(index);
      } else {
        return recDataBuilder_.getMessage(index);
      }
    }
    /**
     * <code>repeated .com.plato.recoserver.grpc.service.RecData rec_data = 3;</code>
     */
    public Builder setRecData(
        int index, com.plato.recoserver.grpc.service.RecData value) {
      if (recDataBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureRecDataIsMutable();
        recData_.set(index, value);
        onChanged();
      } else {
        recDataBuilder_.setMessage(index, value);
      }
      return this;
    }
    /**
     * <code>repeated .com.plato.recoserver.grpc.service.RecData rec_data = 3;</code>
     */
    public Builder setRecData(
        int index, com.plato.recoserver.grpc.service.RecData.Builder builderForValue) {
      if (recDataBuilder_ == null) {
        ensureRecDataIsMutable();
        recData_.set(index, builderForValue.build());
        onChanged();
      } else {
        recDataBuilder_.setMessage(index, builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .com.plato.recoserver.grpc.service.RecData rec_data = 3;</code>
     */
    public Builder addRecData(com.plato.recoserver.grpc.service.RecData value) {
      if (recDataBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureRecDataIsMutable();
        recData_.add(value);
        onChanged();
      } else {
        recDataBuilder_.addMessage(value);
      }
      return this;
    }
    /**
     * <code>repeated .com.plato.recoserver.grpc.service.RecData rec_data = 3;</code>
     */
    public Builder addRecData(
        int index, com.plato.recoserver.grpc.service.RecData value) {
      if (recDataBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureRecDataIsMutable();
        recData_.add(index, value);
        onChanged();
      } else {
        recDataBuilder_.addMessage(index, value);
      }
      return this;
    }
    /**
     * <code>repeated .com.plato.recoserver.grpc.service.RecData rec_data = 3;</code>
     */
    public Builder addRecData(
        com.plato.recoserver.grpc.service.RecData.Builder builderForValue) {
      if (recDataBuilder_ == null) {
        ensureRecDataIsMutable();
        recData_.add(builderForValue.build());
        onChanged();
      } else {
        recDataBuilder_.addMessage(builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .com.plato.recoserver.grpc.service.RecData rec_data = 3;</code>
     */
    public Builder addRecData(
        int index, com.plato.recoserver.grpc.service.RecData.Builder builderForValue) {
      if (recDataBuilder_ == null) {
        ensureRecDataIsMutable();
        recData_.add(index, builderForValue.build());
        onChanged();
      } else {
        recDataBuilder_.addMessage(index, builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .com.plato.recoserver.grpc.service.RecData rec_data = 3;</code>
     */
    public Builder addAllRecData(
        java.lang.Iterable<? extends com.plato.recoserver.grpc.service.RecData> values) {
      if (recDataBuilder_ == null) {
        ensureRecDataIsMutable();
        com.google.protobuf.AbstractMessageLite.Builder.addAll(
            values, recData_);
        onChanged();
      } else {
        recDataBuilder_.addAllMessages(values);
      }
      return this;
    }
    /**
     * <code>repeated .com.plato.recoserver.grpc.service.RecData rec_data = 3;</code>
     */
    public Builder clearRecData() {
      if (recDataBuilder_ == null) {
        recData_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000001);
        onChanged();
      } else {
        recDataBuilder_.clear();
      }
      return this;
    }
    /**
     * <code>repeated .com.plato.recoserver.grpc.service.RecData rec_data = 3;</code>
     */
    public Builder removeRecData(int index) {
      if (recDataBuilder_ == null) {
        ensureRecDataIsMutable();
        recData_.remove(index);
        onChanged();
      } else {
        recDataBuilder_.remove(index);
      }
      return this;
    }
    /**
     * <code>repeated .com.plato.recoserver.grpc.service.RecData rec_data = 3;</code>
     */
    public com.plato.recoserver.grpc.service.RecData.Builder getRecDataBuilder(
        int index) {
      return getRecDataFieldBuilder().getBuilder(index);
    }
    /**
     * <code>repeated .com.plato.recoserver.grpc.service.RecData rec_data = 3;</code>
     */
    public com.plato.recoserver.grpc.service.RecDataOrBuilder getRecDataOrBuilder(
        int index) {
      if (recDataBuilder_ == null) {
        return recData_.get(index);  } else {
        return recDataBuilder_.getMessageOrBuilder(index);
      }
    }
    /**
     * <code>repeated .com.plato.recoserver.grpc.service.RecData rec_data = 3;</code>
     */
    public java.util.List<? extends com.plato.recoserver.grpc.service.RecDataOrBuilder> 
         getRecDataOrBuilderList() {
      if (recDataBuilder_ != null) {
        return recDataBuilder_.getMessageOrBuilderList();
      } else {
        return java.util.Collections.unmodifiableList(recData_);
      }
    }
    /**
     * <code>repeated .com.plato.recoserver.grpc.service.RecData rec_data = 3;</code>
     */
    public com.plato.recoserver.grpc.service.RecData.Builder addRecDataBuilder() {
      return getRecDataFieldBuilder().addBuilder(
          com.plato.recoserver.grpc.service.RecData.getDefaultInstance());
    }
    /**
     * <code>repeated .com.plato.recoserver.grpc.service.RecData rec_data = 3;</code>
     */
    public com.plato.recoserver.grpc.service.RecData.Builder addRecDataBuilder(
        int index) {
      return getRecDataFieldBuilder().addBuilder(
          index, com.plato.recoserver.grpc.service.RecData.getDefaultInstance());
    }
    /**
     * <code>repeated .com.plato.recoserver.grpc.service.RecData rec_data = 3;</code>
     */
    public java.util.List<com.plato.recoserver.grpc.service.RecData.Builder> 
         getRecDataBuilderList() {
      return getRecDataFieldBuilder().getBuilderList();
    }
    private com.google.protobuf.RepeatedFieldBuilderV3<
        com.plato.recoserver.grpc.service.RecData, com.plato.recoserver.grpc.service.RecData.Builder, com.plato.recoserver.grpc.service.RecDataOrBuilder> 
        getRecDataFieldBuilder() {
      if (recDataBuilder_ == null) {
        recDataBuilder_ = new com.google.protobuf.RepeatedFieldBuilderV3<
            com.plato.recoserver.grpc.service.RecData, com.plato.recoserver.grpc.service.RecData.Builder, com.plato.recoserver.grpc.service.RecDataOrBuilder>(
                recData_,
                ((bitField0_ & 0x00000001) != 0),
                getParentForChildren(),
                isClean());
        recData_ = null;
      }
      return recDataBuilder_;
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


    // @@protoc_insertion_point(builder_scope:com.plato.recoserver.grpc.service.RecoResponse)
  }

  // @@protoc_insertion_point(class_scope:com.plato.recoserver.grpc.service.RecoResponse)
  private static final com.plato.recoserver.grpc.service.RecoResponse DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new com.plato.recoserver.grpc.service.RecoResponse();
  }

  public static com.plato.recoserver.grpc.service.RecoResponse getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<RecoResponse>
      PARSER = new com.google.protobuf.AbstractParser<RecoResponse>() {
    @java.lang.Override
    public RecoResponse parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new RecoResponse(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<RecoResponse> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<RecoResponse> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public com.plato.recoserver.grpc.service.RecoResponse getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}
