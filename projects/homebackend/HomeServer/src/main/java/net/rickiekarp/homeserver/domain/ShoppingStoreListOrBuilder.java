// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: net/rickiekarp/homeserver/ShoppingStores.proto

package net.rickiekarp.homeserver.domain;

public interface ShoppingStoreListOrBuilder extends
    // @@protoc_insertion_point(interface_extends:ShoppingStoreList)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>repeated .ShoppingStore store = 1;</code>
   */
  java.util.List<net.rickiekarp.homeserver.domain.ShoppingStore> 
      getStoreList();
  /**
   * <code>repeated .ShoppingStore store = 1;</code>
   */
  net.rickiekarp.homeserver.domain.ShoppingStore getStore(int index);
  /**
   * <code>repeated .ShoppingStore store = 1;</code>
   */
  int getStoreCount();
  /**
   * <code>repeated .ShoppingStore store = 1;</code>
   */
  java.util.List<? extends net.rickiekarp.homeserver.domain.ShoppingStoreOrBuilder> 
      getStoreOrBuilderList();
  /**
   * <code>repeated .ShoppingStore store = 1;</code>
   */
  net.rickiekarp.homeserver.domain.ShoppingStoreOrBuilder getStoreOrBuilder(
      int index);
}