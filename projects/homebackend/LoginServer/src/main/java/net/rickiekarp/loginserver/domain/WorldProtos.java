// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: net/rickiekarp/loginserver/WorldProto.proto

package net.rickiekarp.loginserver.domain;

public final class WorldProtos {
  private WorldProtos() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_World_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_World_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_WorldList_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_WorldList_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n+net/rickiekarp/loginserver/WorldProto." +
      "proto\"E\n\005World\022\n\n\002id\030\001 \001(\005\022\014\n\004name\030\002 \001(\t" +
      "\022\013\n\003url\030\003 \001(\t\022\025\n\rworldstatusid\030\004 \001(\005\"\"\n\t" +
      "WorldList\022\025\n\005world\030\001 \003(\0132\006.WorldB2\n!net." +
      "rickiekarp.loginserver.domainB\013WorldProt" +
      "osP\001b\006proto3"
    };
    descriptor = com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        });
    internal_static_World_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_World_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_World_descriptor,
        new java.lang.String[] { "Id", "Name", "Url", "Worldstatusid", });
    internal_static_WorldList_descriptor =
      getDescriptor().getMessageTypes().get(1);
    internal_static_WorldList_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_WorldList_descriptor,
        new java.lang.String[] { "World", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
