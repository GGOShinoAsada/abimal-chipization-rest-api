package com.example.demo.config;

import com.example.demo.model.AnimalType;
import com.example.demo.repository.AnimalTypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.AttributeConverter;
import java.util.*;

@Slf4j
@Component
public class AnimalTypeListConverter implements AttributeConverter<Set<AnimalType>, String> {

    @Autowired
    private AnimalTypeRepository repository;

    @Override
    public String convertToDatabaseColumn(Set<AnimalType> attribute) {
        log.info("convert array of long to string");
        String output = "";
        if (attribute!=null)
        {
            for (AnimalType item : attribute)
            {
                output+=item.getId()+',';
            }
            output = output.substring(0, output.length()-2);
        }
        else {
            log.warn("input data is empty");
        }
        return output;
    }

    @Override
    public Set<AnimalType> convertToEntityAttribute(String data) {
        log.info("convert string to list of long");
        Set<AnimalType> output = new HashSet();
        if (data!=null && !data.equals(""))
        {
            try{
                List<String> list = Arrays.asList(data.split(","));
                for (String value : list)
                {
                    long id = Long.parseLong(value);
                    Optional<AnimalType> box = repository.findById(id);
                    if (box.isPresent())
                    {
                        output.add(box.get());
                    }
                    else
                    {
                        log.warn("element was not found");
                        output = new HashSet();
                        break;
                    }
                }
            }
            catch (IllegalArgumentException ex)
            {
                log.error("input data contains repeat elements");
                output = new HashSet();
            }
            catch (Exception ex)
            {
                log.error(ex.getStackTrace().toString());
                output = new HashSet();
            }
        }
        else {
            log.warn("input data is empty");
        }
        return output;
    }
}
