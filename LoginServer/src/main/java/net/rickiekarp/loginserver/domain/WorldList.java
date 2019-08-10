// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: net/rickiekarp/loginserver/WorldProto.proto

package net.rickiekarp.loginserver.domain;

/**
 * Protobuf type {@code WorldList}
 */
public  final class WorldList extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:WorldList)
    WorldListOrBuilder {
private static final long serialVersionUID = 0L;
  // Use WorldList.newBuilder() to construct.
  private WorldList(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private WorldList() {
    world_ = java.util.Collections.emptyList();
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new WorldList();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private WorldList(
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
              world_ = new java.util.ArrayList<net.rickiekarp.loginserver.domain.World>();
              mutable_bitField0_ |= 0x00000001;
            }
            world_.add(
                input.readMessage(net.rickiekarp.loginserver.domain.World.parser(), extensionRegistry));
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
        world_ = java.util.Collections.unmodifiableList(world_);
      }
      this.unknownFields = unknownFields.build();
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return net.rickiekarp.loginserver.domain.WorldProtos.internal_static_WorldList_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return net.rickiekarp.loginserver.domain.WorldProtos.internal_static_WorldList_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            net.rickiekarp.loginserver.domain.WorldList.class, net.rickiekarp.loginserver.domain.WorldList.Builder.class);
  }

  public static final int WORLD_FIELD_NUMBER = 1;
  private java.util.List<net.rickiekarp.loginserver.domain.World> world_;
  /**
   * <code>repeated .World world = 1;</code>
   */
  public java.util.List<net.rickiekarp.loginserver.domain.World> getWorldList() {
    return world_;
  }
  /**
   * <code>repeated .World world = 1;</code>
   */
  public java.util.List<? extends net.rickiekarp.loginserver.domain.WorldOrBuilder> 
      getWorldOrBuilderList() {
    return world_;
  }
  /**
   * <code>repeated .World world = 1;</code>
   */
  public int getWorldCount() {
    return world_.size();
  }
  /**
   * <code>repeated .World world = 1;</code>
   */
  public net.rickiekarp.loginserver.domain.World getWorld(int index) {
    return world_.get(index);
  }
  /**
   * <code>repeated .World world = 1;</code>
   */
  public net.rickiekarp.loginserver.domain.WorldOrBuilder getWorldOrBuilder(
      int index) {
    return world_.get(index);
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
    for (int i = 0; i < world_.size(); i++) {
      output.writeMessage(1, world_.get(i));
    }
    unknownFields.writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    for (int i = 0; i < world_.size(); i++) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(1, world_.get(i));
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
    if (!(obj instanceof net.rickiekarp.loginserver.domain.WorldList)) {
      return super.equals(obj);
    }
    net.rickiekarp.loginserver.domain.WorldList other = (net.rickiekarp.loginserver.domain.WorldList) obj;

    if (!getWorldList()
        .equals(other.getWorldList())) return false;
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
    if (getWorldCount() > 0) {
      hash = (37 * hash) + WORLD_FIELD_NUMBER;
      hash = (53 * hash) + getWorldList().hashCode();
    }
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static net.rickiekarp.loginserver.domain.WorldList parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static net.rickiekarp.loginserver.domain.WorldList parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static net.rickiekarp.loginserver.domain.WorldList parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static net.rickiekarp.loginserver.domain.WorldList parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static net.rickiekarp.loginserver.domain.WorldList parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static net.rickiekarp.loginserver.domain.WorldList parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static net.rickiekarp.loginserver.domain.WorldList parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static net.rickiekarp.loginserver.domain.WorldList parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static net.rickiekarp.loginserver.domain.WorldList parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static net.rickiekarp.loginserver.domain.WorldList parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static net.rickiekarp.loginserver.domain.WorldList parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static net.rickiekarp.loginserver.domain.WorldList parseFrom(
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
  public static Builder newBuilder(net.rickiekarp.loginserver.domain.WorldList prototype) {
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
   * Protobuf type {@code WorldList}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:WorldList)
      net.rickiekarp.loginserver.domain.WorldListOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return net.rickiekarp.loginserver.domain.WorldProtos.internal_static_WorldList_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return net.rickiekarp.loginserver.domain.WorldProtos.internal_static_WorldList_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              net.rickiekarp.loginserver.domain.WorldList.class, net.rickiekarp.loginserver.domain.WorldList.Builder.class);
    }

    // Construct using net.rickiekarp.loginserver.domain.WorldList.newBuilder()
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
        getWorldFieldBuilder();
      }
    }
    @java.lang.Override
    public Builder clear() {
      super.clear();
      if (worldBuilder_ == null) {
        world_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000001);
      } else {
        worldBuilder_.clear();
      }
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return net.rickiekarp.loginserver.domain.WorldProtos.internal_static_WorldList_descriptor;
    }

    @java.lang.Override
    public net.rickiekarp.loginserver.domain.WorldList getDefaultInstanceForType() {
      return net.rickiekarp.loginserver.domain.WorldList.getDefaultInstance();
    }

    @java.lang.Override
    public net.rickiekarp.loginserver.domain.WorldList build() {
      net.rickiekarp.loginserver.domain.WorldList result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public net.rickiekarp.loginserver.domain.WorldList buildPartial() {
      net.rickiekarp.loginserver.domain.WorldList result = new net.rickiekarp.loginserver.domain.WorldList(this);
      int from_bitField0_ = bitField0_;
      if (worldBuilder_ == null) {
        if (((bitField0_ & 0x00000001) != 0)) {
          world_ = java.util.Collections.unmodifiableList(world_);
          bitField0_ = (bitField0_ & ~0x00000001);
        }
        result.world_ = world_;
      } else {
        result.world_ = worldBuilder_.build();
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
      if (other instanceof net.rickiekarp.loginserver.domain.WorldList) {
        return mergeFrom((net.rickiekarp.loginserver.domain.WorldList)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(net.rickiekarp.loginserver.domain.WorldList other) {
      if (other == net.rickiekarp.loginserver.domain.WorldList.getDefaultInstance()) return this;
      if (worldBuilder_ == null) {
        if (!other.world_.isEmpty()) {
          if (world_.isEmpty()) {
            world_ = other.world_;
            bitField0_ = (bitField0_ & ~0x00000001);
          } else {
            ensureWorldIsMutable();
            world_.addAll(other.world_);
          }
          onChanged();
        }
      } else {
        if (!other.world_.isEmpty()) {
          if (worldBuilder_.isEmpty()) {
            worldBuilder_.dispose();
            worldBuilder_ = null;
            world_ = other.world_;
            bitField0_ = (bitField0_ & ~0x00000001);
            worldBuilder_ = 
              com.google.protobuf.GeneratedMessageV3.alwaysUseFieldBuilders ?
                 getWorldFieldBuilder() : null;
          } else {
            worldBuilder_.addAllMessages(other.world_);
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
      net.rickiekarp.loginserver.domain.WorldList parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (net.rickiekarp.loginserver.domain.WorldList) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }
    private int bitField0_;

    private java.util.List<net.rickiekarp.loginserver.domain.World> world_ =
      java.util.Collections.emptyList();
    private void ensureWorldIsMutable() {
      if (!((bitField0_ & 0x00000001) != 0)) {
        world_ = new java.util.ArrayList<net.rickiekarp.loginserver.domain.World>(world_);
        bitField0_ |= 0x00000001;
       }
    }

    private com.google.protobuf.RepeatedFieldBuilderV3<
        net.rickiekarp.loginserver.domain.World, net.rickiekarp.loginserver.domain.World.Builder, net.rickiekarp.loginserver.domain.WorldOrBuilder> worldBuilder_;

    /**
     * <code>repeated .World world = 1;</code>
     */
    public java.util.List<net.rickiekarp.loginserver.domain.World> getWorldList() {
      if (worldBuilder_ == null) {
        return java.util.Collections.unmodifiableList(world_);
      } else {
        return worldBuilder_.getMessageList();
      }
    }
    /**
     * <code>repeated .World world = 1;</code>
     */
    public int getWorldCount() {
      if (worldBuilder_ == null) {
        return world_.size();
      } else {
        return worldBuilder_.getCount();
      }
    }
    /**
     * <code>repeated .World world = 1;</code>
     */
    public net.rickiekarp.loginserver.domain.World getWorld(int index) {
      if (worldBuilder_ == null) {
        return world_.get(index);
      } else {
        return worldBuilder_.getMessage(index);
      }
    }
    /**
     * <code>repeated .World world = 1;</code>
     */
    public Builder setWorld(
        int index, net.rickiekarp.loginserver.domain.World value) {
      if (worldBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureWorldIsMutable();
        world_.set(index, value);
        onChanged();
      } else {
        worldBuilder_.setMessage(index, value);
      }
      return this;
    }
    /**
     * <code>repeated .World world = 1;</code>
     */
    public Builder setWorld(
        int index, net.rickiekarp.loginserver.domain.World.Builder builderForValue) {
      if (worldBuilder_ == null) {
        ensureWorldIsMutable();
        world_.set(index, builderForValue.build());
        onChanged();
      } else {
        worldBuilder_.setMessage(index, builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .World world = 1;</code>
     */
    public Builder addWorld(net.rickiekarp.loginserver.domain.World value) {
      if (worldBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureWorldIsMutable();
        world_.add(value);
        onChanged();
      } else {
        worldBuilder_.addMessage(value);
      }
      return this;
    }
    /**
     * <code>repeated .World world = 1;</code>
     */
    public Builder addWorld(
        int index, net.rickiekarp.loginserver.domain.World value) {
      if (worldBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureWorldIsMutable();
        world_.add(index, value);
        onChanged();
      } else {
        worldBuilder_.addMessage(index, value);
      }
      return this;
    }
    /**
     * <code>repeated .World world = 1;</code>
     */
    public Builder addWorld(
        net.rickiekarp.loginserver.domain.World.Builder builderForValue) {
      if (worldBuilder_ == null) {
        ensureWorldIsMutable();
        world_.add(builderForValue.build());
        onChanged();
      } else {
        worldBuilder_.addMessage(builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .World world = 1;</code>
     */
    public Builder addWorld(
        int index, net.rickiekarp.loginserver.domain.World.Builder builderForValue) {
      if (worldBuilder_ == null) {
        ensureWorldIsMutable();
        world_.add(index, builderForValue.build());
        onChanged();
      } else {
        worldBuilder_.addMessage(index, builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .World world = 1;</code>
     */
    public Builder addAllWorld(
        java.lang.Iterable<? extends net.rickiekarp.loginserver.domain.World> values) {
      if (worldBuilder_ == null) {
        ensureWorldIsMutable();
        com.google.protobuf.AbstractMessageLite.Builder.addAll(
            values, world_);
        onChanged();
      } else {
        worldBuilder_.addAllMessages(values);
      }
      return this;
    }
    /**
     * <code>repeated .World world = 1;</code>
     */
    public Builder clearWorld() {
      if (worldBuilder_ == null) {
        world_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000001);
        onChanged();
      } else {
        worldBuilder_.clear();
      }
      return this;
    }
    /**
     * <code>repeated .World world = 1;</code>
     */
    public Builder removeWorld(int index) {
      if (worldBuilder_ == null) {
        ensureWorldIsMutable();
        world_.remove(index);
        onChanged();
      } else {
        worldBuilder_.remove(index);
      }
      return this;
    }
    /**
     * <code>repeated .World world = 1;</code>
     */
    public net.rickiekarp.loginserver.domain.World.Builder getWorldBuilder(
        int index) {
      return getWorldFieldBuilder().getBuilder(index);
    }
    /**
     * <code>repeated .World world = 1;</code>
     */
    public net.rickiekarp.loginserver.domain.WorldOrBuilder getWorldOrBuilder(
        int index) {
      if (worldBuilder_ == null) {
        return world_.get(index);  } else {
        return worldBuilder_.getMessageOrBuilder(index);
      }
    }
    /**
     * <code>repeated .World world = 1;</code>
     */
    public java.util.List<? extends net.rickiekarp.loginserver.domain.WorldOrBuilder> 
         getWorldOrBuilderList() {
      if (worldBuilder_ != null) {
        return worldBuilder_.getMessageOrBuilderList();
      } else {
        return java.util.Collections.unmodifiableList(world_);
      }
    }
    /**
     * <code>repeated .World world = 1;</code>
     */
    public net.rickiekarp.loginserver.domain.World.Builder addWorldBuilder() {
      return getWorldFieldBuilder().addBuilder(
          net.rickiekarp.loginserver.domain.World.getDefaultInstance());
    }
    /**
     * <code>repeated .World world = 1;</code>
     */
    public net.rickiekarp.loginserver.domain.World.Builder addWorldBuilder(
        int index) {
      return getWorldFieldBuilder().addBuilder(
          index, net.rickiekarp.loginserver.domain.World.getDefaultInstance());
    }
    /**
     * <code>repeated .World world = 1;</code>
     */
    public java.util.List<net.rickiekarp.loginserver.domain.World.Builder> 
         getWorldBuilderList() {
      return getWorldFieldBuilder().getBuilderList();
    }
    private com.google.protobuf.RepeatedFieldBuilderV3<
        net.rickiekarp.loginserver.domain.World, net.rickiekarp.loginserver.domain.World.Builder, net.rickiekarp.loginserver.domain.WorldOrBuilder> 
        getWorldFieldBuilder() {
      if (worldBuilder_ == null) {
        worldBuilder_ = new com.google.protobuf.RepeatedFieldBuilderV3<
            net.rickiekarp.loginserver.domain.World, net.rickiekarp.loginserver.domain.World.Builder, net.rickiekarp.loginserver.domain.WorldOrBuilder>(
                world_,
                ((bitField0_ & 0x00000001) != 0),
                getParentForChildren(),
                isClean());
        world_ = null;
      }
      return worldBuilder_;
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


    // @@protoc_insertion_point(builder_scope:WorldList)
  }

  // @@protoc_insertion_point(class_scope:WorldList)
  private static final net.rickiekarp.loginserver.domain.WorldList DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new net.rickiekarp.loginserver.domain.WorldList();
  }

  public static net.rickiekarp.loginserver.domain.WorldList getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<WorldList>
      PARSER = new com.google.protobuf.AbstractParser<WorldList>() {
    @java.lang.Override
    public WorldList parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new WorldList(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<WorldList> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<WorldList> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public net.rickiekarp.loginserver.domain.WorldList getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

