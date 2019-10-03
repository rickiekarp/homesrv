// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: net/rickiekarp/homeserver/ShoppingNote.proto

package net.rickiekarp.homeserver.domain;

public interface ShoppingNoteOrBuilder extends
    // @@protoc_insertion_point(interface_extends:ShoppingNote)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>int32 id = 1;</code>
   */
  int getId();

  /**
   * <code>string title = 2;</code>
   */
  java.lang.String getTitle();
  /**
   * <code>string title = 2;</code>
   */
  com.google.protobuf.ByteString
      getTitleBytes();

  /**
   * <code>double price = 3;</code>
   */
  double getPrice();

  /**
   * <code>int32 user_id = 4;</code>
   */
  int getUserId();

  /**
   * <code>.google.protobuf.Timestamp dateBought = 5;</code>
   */
  boolean hasDateBought();
  /**
   * <code>.google.protobuf.Timestamp dateBought = 5;</code>
   */
  com.google.protobuf.Timestamp getDateBought();
  /**
   * <code>.google.protobuf.Timestamp dateBought = 5;</code>
   */
  com.google.protobuf.TimestampOrBuilder getDateBoughtOrBuilder();

  /**
   * <code>int32 store_id = 6;</code>
   */
  int getStoreId();

  /**
   * <code>.google.protobuf.Timestamp dateAdded = 7;</code>
   */
  boolean hasDateAdded();
  /**
   * <code>.google.protobuf.Timestamp dateAdded = 7;</code>
   */
  com.google.protobuf.Timestamp getDateAdded();
  /**
   * <code>.google.protobuf.Timestamp dateAdded = 7;</code>
   */
  com.google.protobuf.TimestampOrBuilder getDateAddedOrBuilder();

  /**
   * <code>.google.protobuf.Timestamp lastUpdated = 8;</code>
   */
  boolean hasLastUpdated();
  /**
   * <code>.google.protobuf.Timestamp lastUpdated = 8;</code>
   */
  com.google.protobuf.Timestamp getLastUpdated();
  /**
   * <code>.google.protobuf.Timestamp lastUpdated = 8;</code>
   */
  com.google.protobuf.TimestampOrBuilder getLastUpdatedOrBuilder();
}
