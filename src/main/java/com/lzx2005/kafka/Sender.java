package com.lzx2005.kafka;


import org.apache.kafka.clients.producer.*;
import java.util.Properties;


public class Sender {


    public void doSend(String msg){
        Properties props = new Properties();
        //props.put("schema.registry.url", "http://10.1.1.81:8081");
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "10.1.1.25:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        KafkaProducer producer = new KafkaProducer(props);
        producer.send(new ProducerRecord<>("test1",msg), new Callback() {
            @Override
            public void onCompletion(RecordMetadata metadata, Exception exception) {
                if(null!=exception){
                    System.out.println("[发送失败]"+exception.getMessage());
                }else{
                    System.out.println("[发送成功]"+metadata.toString());
                }
            }
        });
        producer.close();
    }


    public static void main(String[] args) {
        new Sender().doSend("test");
    }
}
