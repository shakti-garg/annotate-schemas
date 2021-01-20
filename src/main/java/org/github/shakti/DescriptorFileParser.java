package org.github.shakti;

import com.google.protobuf.*;
import playground.v1.BusinessTermDescriptor;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DescriptorFileParser {

    public static void main(String[] args) throws IOException, Descriptors.DescriptorValidationException {

        ExtensionRegistry extensionRegistry = ExtensionRegistry.newInstance();
        extensionRegistry.add(BusinessTermDescriptor.bizTerm);

        FileInputStream fin = new FileInputStream("src/main/resources/message_sample.desc");
        DescriptorProtos.FileDescriptorSet set = DescriptorProtos.FileDescriptorSet.parseFrom(fin, extensionRegistry);

        List<Descriptors.FileDescriptor> dependencyFileDescriptorList = new ArrayList<>();
        for(int i=2; i<set.getFileCount()-1; i++) {
            dependencyFileDescriptorList.add(Descriptors.FileDescriptor.buildFrom(set.getFile(i), dependencyFileDescriptorList.toArray(new Descriptors.FileDescriptor[0])));
        }

        //retrieve last proto message (all previous to it will be deps)
        Descriptors.FileDescriptor fd = Descriptors.FileDescriptor.buildFrom(set.getFile(set.getFileCount()-1), dependencyFileDescriptorList.toArray(new Descriptors.FileDescriptor[0]));

        System.out.println("=====Top-level Schema======");
        System.out.println(fd.toProto());

        for (Map.Entry<Descriptors.FieldDescriptor, Object> fileOption : fd.getOptions().getAllFields().entrySet()) {
            System.out.println("=====File Options======");
            System.out.println("name: " + fileOption.getKey().getName());
            System.out.println("value: " + fileOption.getValue());
        }

        for (Descriptors.Descriptor msgType : fd.getMessageTypes()) {
            printMessageSchema(msgType);
        }
    }

    private static void printMessageSchema(Descriptors.Descriptor msgType) {
        System.out.println("=====Message Schema======");
        System.out.println("name: " + msgType.getName());
        System.out.println("full name: " + msgType.getFields());

        for (Map.Entry<Descriptors.FieldDescriptor, Object> msgOption : msgType.getOptions().getAllFields().entrySet()) {
            System.out.println("=====Message Options======");
            System.out.println("name: " + msgOption.getKey().getName());
            System.out.println("value: " + msgOption.getValue());
        }

        System.out.println("=====Field Schema======");
        for (Descriptors.FieldDescriptor fieldType : msgType.getFields()) {
            System.out.println("name: " + fieldType.getName());
            System.out.println("type: " + fieldType.getType());
            System.out.println("full name: " + fieldType.getFullName());

            for (Map.Entry<Descriptors.FieldDescriptor, Object> fieldOption : fieldType.getOptions().getAllFields().entrySet()) {
                System.out.println("=====Field Options======");
                System.out.println("name: " + fieldOption.getKey().getName());
                System.out.println("value: " + fieldOption.getValue());
            }

            if(fieldType.getType() == Descriptors.FieldDescriptor.Type.MESSAGE){
                printMessageSchema(fieldType.getMessageType());
            }
            System.out.println("=====================");
        }
    }

}
