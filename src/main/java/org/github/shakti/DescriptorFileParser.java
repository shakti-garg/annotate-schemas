//package org.github.shakti;
//
//import com.google.protobuf.DescriptorProtos;
//import com.google.protobuf.Descriptors;
//import com.google.protobuf.InvalidProtocolBufferException;
//
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Files;
//
//public class DescriptorFileParser {
//
//    public static void main(String[] args) throws IOException, Descriptors.DescriptorValidationException {
//        byte[] schemaBytes = Files.readAllBytes(new File("src/main/resources/quote_sample_new.desc").toPath());
//        Descriptors.FileDescriptor fileDescriptor = parseProto(schemaBytes);
//
//        System.out.println(fileDescriptor.toProto());
//    }
//
//    public static Descriptors.FileDescriptor parseProto (byte[] schemaBytes) throws InvalidProtocolBufferException, Descriptors.DescriptorValidationException {
//        DescriptorProtos.FileDescriptorProto descriptorProto = DescriptorProtos.FileDescriptorProto.parseFrom(schemaBytes);
//        return Descriptors.FileDescriptor.buildFrom(descriptorProto, new Descriptors.FileDescriptor[0]);
//    }
//}
