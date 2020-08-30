package com.interswitch.smartmoveserver.util;

import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;

public class FileParser<T> {

    private static final CsvMapper mapper = new CsvMapper();

    public List<T> parseFileToEntity(MultipartFile file,Class<T> model) throws IOException {
        return read(model, file.getInputStream());
    }

    private <T> List<T> read(Class<T> model, InputStream stream) throws IOException {
        CsvSchema schema = mapper.schemaFor(model).withHeader().withColumnReordering(true);
        ObjectReader reader = mapper.readerFor(model).with(schema);
        return reader.<T>readValues(stream).readAll();
    }

        /*
        public static <T> void write(File file,List<CustomerDTO> customersDto) throws IOException {

            System.out.println("wanna writ to file");
            // initialize and configure the mapper
            CsvMapper mapper = new CsvMapper();
            // we ignore unknown fields or fields not specified in schema, otherwise
            System.out.println("wanna writ to file..1");
            // writing will fail
            mapper.configure(JsonGenerator.Feature.IGNORE_UNKNOWN, true);
            System.out.println("wanna writ to file...2");
            // initialize the schema
            //CsvSchema schema = mapper.schemaFor(model).withHeader().withColumnReordering(true);
            CsvSchema schema = CsvSchema.builder()
                    .addColumn("AccountID")
                    .addColumn("MeterId")
                    .addColumn("ShopNumber")
                    .addColumn("PhoneNumber")
                    .addColumn("FirstName")
                    .addColumn("LastName")
                    .addColumn("Email")
                    .addColumn("Line")
                    .addColumn("Market")
                    .addColumn("Tier").setStrictHeaders(true)
                    .setStrictHeaders(true)
                    .setUseHeader(true)
                    .build();

            System.out.println("wanna writ to file...3");
            // map the bean with our schema for the writer
            ObjectWriter writer = mapper.writerFor(CustomerDTO.class).with(schema);
            System.out.println("wanna writ to file...4");
            // we write the list of objects
            writer.writeValues(file).writeAll(customersDto);
            System.out.println("wanna writ to file...5");
            // we write the list of objects
            return;
        }
        @
         */
}
