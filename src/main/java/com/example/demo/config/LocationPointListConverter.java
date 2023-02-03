package com.example.demo.config;

import com.example.demo.model.LocationPoint;
import com.example.demo.repository.LocationPointRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.AttributeConverter;
import java.util.*;

@Slf4j
@Component
public class LocationPointListConverter implements AttributeConverter<Set<LocationPoint>, String> {

    @Autowired
    private LocationPointRepository repository;

    @Override
    public String convertToDatabaseColumn(Set<LocationPoint> attribute) {
        log.info("convert list of long to string");
        String output = "";
        if (attribute!=null)
        {
            for (LocationPoint item : attribute)
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
    public Set<LocationPoint> convertToEntityAttribute(String data) {
        log.info("convert string to array of long");
        Set<LocationPoint> output = new HashSet();
        if (data!=null)
        {
            try{
                List<String> list = Arrays.asList(data.split(","));
                for (String value : list)
                {
                    long id = Long.parseLong(value);
                    Optional<LocationPoint> box = repository.findById(id);
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
