// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: net/rickiekarp/homeserver/ShoppingStores.proto

package net.rickiekarp.homeserver.domain;

public final class ShoppingStoreProtos {
  private ShoppingStoreProtos() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_ShoppingStore_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_ShoppingStore_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_ShoppingStoreList_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_ShoppingStoreList_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n.net/rickiekarp/homeserver/ShoppingStor" +
      "es.proto\")\n\rShoppingStore\022\n\n\002id\030\001 \001(\005\022\014\n" +
      "\004name\030\002 \001(\t\"2\n\021ShoppingStoreList\022\035\n\005stor" +
      "e\030\001 \003(\0132\016.ShoppingStoreB9\n net.rickiekar" +
      "p.homeserver.domainB\023ShoppingStoreProtos" +
      "P\001b\006proto3"
    };
    descriptor = com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        });
    internal_static_ShoppingStore_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_ShoppingStore_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_ShoppingStore_descriptor,
        new java.lang.String[] { "Id", "Name", });
    internal_static_ShoppingStoreList_descriptor =
      getDescriptor().getMessageTypes().get(1);
    internal_static_ShoppingStoreList_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_ShoppingStoreList_descriptor,
        new java.lang.String[] { "Store", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
