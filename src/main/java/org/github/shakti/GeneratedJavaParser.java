package org.github.shakti;

import com.google.protobuf.Descriptors;
import playground.v1.MessageSampleOuterClass;

import java.io.IOException;
import java.util.Map;

public class GeneratedJavaParser {

    public static void main(String[] args) throws IOException, Descriptors.DescriptorValidationException {

        Descriptors.FileDescriptor fd = MessageSampleOuterClass.getDescriptor();

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
