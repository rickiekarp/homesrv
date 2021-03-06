// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: net/rickiekarp/homeserver/ShoppingNote.proto

package net.rickiekarp.homeserver.domain;

/**
 * Protobuf type {@code ShoppingNoteList}
 */
public final class ShoppingNoteList extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:ShoppingNoteList)
    ShoppingNoteListOrBuilder {
private static final long serialVersionUID = 0L;
  // Use ShoppingNoteList.newBuilder() to construct.
  private ShoppingNoteList(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private ShoppingNoteList() {
    note_ = java.util.Collections.emptyList();
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new ShoppingNoteList();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private ShoppingNoteList(
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
              note_ = new java.util.ArrayList<net.rickiekarp.homeserver.domain.ShoppingNote>();
              mutable_bitField0_ |= 0x00000001;
            }
            note_.add(
                input.readMessage(net.rickiekarp.homeserver.domain.ShoppingNote.parser(), extensionRegistry));
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
        note_ = java.util.Collections.unmodifiableList(note_);
      }
      this.unknownFields = unknownFields.build();
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return net.rickiekarp.homeserver.domain.ShoppingNoteProtos.internal_static_ShoppingNoteList_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return net.rickiekarp.homeserver.domain.ShoppingNoteProtos.internal_static_ShoppingNoteList_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            net.rickiekarp.homeserver.domain.ShoppingNoteList.class, net.rickiekarp.homeserver.domain.ShoppingNoteList.Builder.class);
  }

  public static final int NOTE_FIELD_NUMBER = 1;
  private java.util.List<net.rickiekarp.homeserver.domain.ShoppingNote> note_;
  /**
   * <code>repeated .ShoppingNote note = 1;</code>
   */
  @java.lang.Override
  public java.util.List<net.rickiekarp.homeserver.domain.ShoppingNote> getNoteList() {
    return note_;
  }
  /**
   * <code>repeated .ShoppingNote note = 1;</code>
   */
  @java.lang.Override
  public java.util.List<? extends net.rickiekarp.homeserver.domain.ShoppingNoteOrBuilder> 
      getNoteOrBuilderList() {
    return note_;
  }
  /**
   * <code>repeated .ShoppingNote note = 1;</code>
   */
  @java.lang.Override
  public int getNoteCount() {
    return note_.size();
  }
  /**
   * <code>repeated .ShoppingNote note = 1;</code>
   */
  @java.lang.Override
  public net.rickiekarp.homeserver.domain.ShoppingNote getNote(int index) {
    return note_.get(index);
  }
  /**
   * <code>repeated .ShoppingNote note = 1;</code>
   */
  @java.lang.Override
  public net.rickiekarp.homeserver.domain.ShoppingNoteOrBuilder getNoteOrBuilder(
      int index) {
    return note_.get(index);
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
    for (int i = 0; i < note_.size(); i++) {
      output.writeMessage(1, note_.get(i));
    }
    unknownFields.writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    for (int i = 0; i < note_.size(); i++) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(1, note_.get(i));
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
    if (!(obj instanceof net.rickiekarp.homeserver.domain.ShoppingNoteList)) {
      return super.equals(obj);
    }
    net.rickiekarp.homeserver.domain.ShoppingNoteList other = (net.rickiekarp.homeserver.domain.ShoppingNoteList) obj;

    if (!getNoteList()
        .equals(other.getNoteList())) return false;
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
    if (getNoteCount() > 0) {
      hash = (37 * hash) + NOTE_FIELD_NUMBER;
      hash = (53 * hash) + getNoteList().hashCode();
    }
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static net.rickiekarp.homeserver.domain.ShoppingNoteList parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static net.rickiekarp.homeserver.domain.ShoppingNoteList parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static net.rickiekarp.homeserver.domain.ShoppingNoteList parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static net.rickiekarp.homeserver.domain.ShoppingNoteList parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static net.rickiekarp.homeserver.domain.ShoppingNoteList parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static net.rickiekarp.homeserver.domain.ShoppingNoteList parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static net.rickiekarp.homeserver.domain.ShoppingNoteList parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static net.rickiekarp.homeserver.domain.ShoppingNoteList parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static net.rickiekarp.homeserver.domain.ShoppingNoteList parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static net.rickiekarp.homeserver.domain.ShoppingNoteList parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static net.rickiekarp.homeserver.domain.ShoppingNoteList parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static net.rickiekarp.homeserver.domain.ShoppingNoteList parseFrom(
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
  public static Builder newBuilder(net.rickiekarp.homeserver.domain.ShoppingNoteList prototype) {
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
   * Protobuf type {@code ShoppingNoteList}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:ShoppingNoteList)
      net.rickiekarp.homeserver.domain.ShoppingNoteListOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return net.rickiekarp.homeserver.domain.ShoppingNoteProtos.internal_static_ShoppingNoteList_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return net.rickiekarp.homeserver.domain.ShoppingNoteProtos.internal_static_ShoppingNoteList_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              net.rickiekarp.homeserver.domain.ShoppingNoteList.class, net.rickiekarp.homeserver.domain.ShoppingNoteList.Builder.class);
    }

    // Construct using net.rickiekarp.homeserver.domain.ShoppingNoteList.newBuilder()
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
        getNoteFieldBuilder();
      }
    }
    @java.lang.Override
    public Builder clear() {
      super.clear();
      if (noteBuilder_ == null) {
        note_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000001);
      } else {
        noteBuilder_.clear();
      }
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return net.rickiekarp.homeserver.domain.ShoppingNoteProtos.internal_static_ShoppingNoteList_descriptor;
    }

    @java.lang.Override
    public net.rickiekarp.homeserver.domain.ShoppingNoteList getDefaultInstanceForType() {
      return net.rickiekarp.homeserver.domain.ShoppingNoteList.getDefaultInstance();
    }

    @java.lang.Override
    public net.rickiekarp.homeserver.domain.ShoppingNoteList build() {
      net.rickiekarp.homeserver.domain.ShoppingNoteList result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public net.rickiekarp.homeserver.domain.ShoppingNoteList buildPartial() {
      net.rickiekarp.homeserver.domain.ShoppingNoteList result = new net.rickiekarp.homeserver.domain.ShoppingNoteList(this);
      int from_bitField0_ = bitField0_;
      if (noteBuilder_ == null) {
        if (((bitField0_ & 0x00000001) != 0)) {
          note_ = java.util.Collections.unmodifiableList(note_);
          bitField0_ = (bitField0_ & ~0x00000001);
        }
        result.note_ = note_;
      } else {
        result.note_ = noteBuilder_.build();
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
      if (other instanceof net.rickiekarp.homeserver.domain.ShoppingNoteList) {
        return mergeFrom((net.rickiekarp.homeserver.domain.ShoppingNoteList)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(net.rickiekarp.homeserver.domain.ShoppingNoteList other) {
      if (other == net.rickiekarp.homeserver.domain.ShoppingNoteList.getDefaultInstance()) return this;
      if (noteBuilder_ == null) {
        if (!other.note_.isEmpty()) {
          if (note_.isEmpty()) {
            note_ = other.note_;
            bitField0_ = (bitField0_ & ~0x00000001);
          } else {
            ensureNoteIsMutable();
            note_.addAll(other.note_);
          }
          onChanged();
        }
      } else {
        if (!other.note_.isEmpty()) {
          if (noteBuilder_.isEmpty()) {
            noteBuilder_.dispose();
            noteBuilder_ = null;
            note_ = other.note_;
            bitField0_ = (bitField0_ & ~0x00000001);
            noteBuilder_ = 
              com.google.protobuf.GeneratedMessageV3.alwaysUseFieldBuilders ?
                 getNoteFieldBuilder() : null;
          } else {
            noteBuilder_.addAllMessages(other.note_);
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
      net.rickiekarp.homeserver.domain.ShoppingNoteList parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (net.rickiekarp.homeserver.domain.ShoppingNoteList) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }
    private int bitField0_;

    private java.util.List<net.rickiekarp.homeserver.domain.ShoppingNote> note_ =
      java.util.Collections.emptyList();
    private void ensureNoteIsMutable() {
      if (!((bitField0_ & 0x00000001) != 0)) {
        note_ = new java.util.ArrayList<net.rickiekarp.homeserver.domain.ShoppingNote>(note_);
        bitField0_ |= 0x00000001;
       }
    }

    private com.google.protobuf.RepeatedFieldBuilderV3<
        net.rickiekarp.homeserver.domain.ShoppingNote, net.rickiekarp.homeserver.domain.ShoppingNote.Builder, net.rickiekarp.homeserver.domain.ShoppingNoteOrBuilder> noteBuilder_;

    /**
     * <code>repeated .ShoppingNote note = 1;</code>
     */
    public java.util.List<net.rickiekarp.homeserver.domain.ShoppingNote> getNoteList() {
      if (noteBuilder_ == null) {
        return java.util.Collections.unmodifiableList(note_);
      } else {
        return noteBuilder_.getMessageList();
      }
    }
    /**
     * <code>repeated .ShoppingNote note = 1;</code>
     */
    public int getNoteCount() {
      if (noteBuilder_ == null) {
        return note_.size();
      } else {
        return noteBuilder_.getCount();
      }
    }
    /**
     * <code>repeated .ShoppingNote note = 1;</code>
     */
    public net.rickiekarp.homeserver.domain.ShoppingNote getNote(int index) {
      if (noteBuilder_ == null) {
        return note_.get(index);
      } else {
        return noteBuilder_.getMessage(index);
      }
    }
    /**
     * <code>repeated .ShoppingNote note = 1;</code>
     */
    public Builder setNote(
        int index, net.rickiekarp.homeserver.domain.ShoppingNote value) {
      if (noteBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureNoteIsMutable();
        note_.set(index, value);
        onChanged();
      } else {
        noteBuilder_.setMessage(index, value);
      }
      return this;
    }
    /**
     * <code>repeated .ShoppingNote note = 1;</code>
     */
    public Builder setNote(
        int index, net.rickiekarp.homeserver.domain.ShoppingNote.Builder builderForValue) {
      if (noteBuilder_ == null) {
        ensureNoteIsMutable();
        note_.set(index, builderForValue.build());
        onChanged();
      } else {
        noteBuilder_.setMessage(index, builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .ShoppingNote note = 1;</code>
     */
    public Builder addNote(net.rickiekarp.homeserver.domain.ShoppingNote value) {
      if (noteBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureNoteIsMutable();
        note_.add(value);
        onChanged();
      } else {
        noteBuilder_.addMessage(value);
      }
      return this;
    }
    /**
     * <code>repeated .ShoppingNote note = 1;</code>
     */
    public Builder addNote(
        int index, net.rickiekarp.homeserver.domain.ShoppingNote value) {
      if (noteBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureNoteIsMutable();
        note_.add(index, value);
        onChanged();
      } else {
        noteBuilder_.addMessage(index, value);
      }
      return this;
    }
    /**
     * <code>repeated .ShoppingNote note = 1;</code>
     */
    public Builder addNote(
        net.rickiekarp.homeserver.domain.ShoppingNote.Builder builderForValue) {
      if (noteBuilder_ == null) {
        ensureNoteIsMutable();
        note_.add(builderForValue.build());
        onChanged();
      } else {
        noteBuilder_.addMessage(builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .ShoppingNote note = 1;</code>
     */
    public Builder addNote(
        int index, net.rickiekarp.homeserver.domain.ShoppingNote.Builder builderForValue) {
      if (noteBuilder_ == null) {
        ensureNoteIsMutable();
        note_.add(index, builderForValue.build());
        onChanged();
      } else {
        noteBuilder_.addMessage(index, builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .ShoppingNote note = 1;</code>
     */
    public Builder addAllNote(
        java.lang.Iterable<? extends net.rickiekarp.homeserver.domain.ShoppingNote> values) {
      if (noteBuilder_ == null) {
        ensureNoteIsMutable();
        com.google.protobuf.AbstractMessageLite.Builder.addAll(
            values, note_);
        onChanged();
      } else {
        noteBuilder_.addAllMessages(values);
      }
      return this;
    }
    /**
     * <code>repeated .ShoppingNote note = 1;</code>
     */
    public Builder clearNote() {
      if (noteBuilder_ == null) {
        note_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000001);
        onChanged();
      } else {
        noteBuilder_.clear();
      }
      return this;
    }
    /**
     * <code>repeated .ShoppingNote note = 1;</code>
     */
    public Builder removeNote(int index) {
      if (noteBuilder_ == null) {
        ensureNoteIsMutable();
        note_.remove(index);
        onChanged();
      } else {
        noteBuilder_.remove(index);
      }
      return this;
    }
    /**
     * <code>repeated .ShoppingNote note = 1;</code>
     */
    public net.rickiekarp.homeserver.domain.ShoppingNote.Builder getNoteBuilder(
        int index) {
      return getNoteFieldBuilder().getBuilder(index);
    }
    /**
     * <code>repeated .ShoppingNote note = 1;</code>
     */
    public net.rickiekarp.homeserver.domain.ShoppingNoteOrBuilder getNoteOrBuilder(
        int index) {
      if (noteBuilder_ == null) {
        return note_.get(index);  } else {
        return noteBuilder_.getMessageOrBuilder(index);
      }
    }
    /**
     * <code>repeated .ShoppingNote note = 1;</code>
     */
    public java.util.List<? extends net.rickiekarp.homeserver.domain.ShoppingNoteOrBuilder> 
         getNoteOrBuilderList() {
      if (noteBuilder_ != null) {
        return noteBuilder_.getMessageOrBuilderList();
      } else {
        return java.util.Collections.unmodifiableList(note_);
      }
    }
    /**
     * <code>repeated .ShoppingNote note = 1;</code>
     */
    public net.rickiekarp.homeserver.domain.ShoppingNote.Builder addNoteBuilder() {
      return getNoteFieldBuilder().addBuilder(
          net.rickiekarp.homeserver.domain.ShoppingNote.getDefaultInstance());
    }
    /**
     * <code>repeated .ShoppingNote note = 1;</code>
     */
    public net.rickiekarp.homeserver.domain.ShoppingNote.Builder addNoteBuilder(
        int index) {
      return getNoteFieldBuilder().addBuilder(
          index, net.rickiekarp.homeserver.domain.ShoppingNote.getDefaultInstance());
    }
    /**
     * <code>repeated .ShoppingNote note = 1;</code>
     */
    public java.util.List<net.rickiekarp.homeserver.domain.ShoppingNote.Builder> 
         getNoteBuilderList() {
      return getNoteFieldBuilder().getBuilderList();
    }
    private com.google.protobuf.RepeatedFieldBuilderV3<
        net.rickiekarp.homeserver.domain.ShoppingNote, net.rickiekarp.homeserver.domain.ShoppingNote.Builder, net.rickiekarp.homeserver.domain.ShoppingNoteOrBuilder> 
        getNoteFieldBuilder() {
      if (noteBuilder_ == null) {
        noteBuilder_ = new com.google.protobuf.RepeatedFieldBuilderV3<
            net.rickiekarp.homeserver.domain.ShoppingNote, net.rickiekarp.homeserver.domain.ShoppingNote.Builder, net.rickiekarp.homeserver.domain.ShoppingNoteOrBuilder>(
                note_,
                ((bitField0_ & 0x00000001) != 0),
                getParentForChildren(),
                isClean());
        note_ = null;
      }
      return noteBuilder_;
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


    // @@protoc_insertion_point(builder_scope:ShoppingNoteList)
  }

  // @@protoc_insertion_point(class_scope:ShoppingNoteList)
  private static final net.rickiekarp.homeserver.domain.ShoppingNoteList DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new net.rickiekarp.homeserver.domain.ShoppingNoteList();
  }

  public static net.rickiekarp.homeserver.domain.ShoppingNoteList getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<ShoppingNoteList>
      PARSER = new com.google.protobuf.AbstractParser<ShoppingNoteList>() {
    @java.lang.Override
    public ShoppingNoteList parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new ShoppingNoteList(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<ShoppingNoteList> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<ShoppingNoteList> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public net.rickiekarp.homeserver.domain.ShoppingNoteList getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

