package com.example.demo.config;

import com.example.demo.model.AnimalVisitedLocation;
import com.example.demo.service.AnimalVisitedLocationService;
import com.example.demo.service.dto.AnimalVisitedLocationDto;
import com.example.demo.service.mapper.AnimalVisitedLocationMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.AttributeConverter;
import java.util.*;

/**
 * сериализация и десериация посещенных точек локации для сущности Animal
 * @author ROMAN
 * @date 2023-02-17
 * @version 1.0
 */
@Slf4j
@Component
public class AnimalVisitedLocationPointConverter implements AttributeConverter<List<AnimalVisitedLocation>, String> {


    private AnimalVisitedLocationService animalVisitedLocationService;

    private AnimalVisitedLocationMapper animalVisitedLocationMapper;


    @Autowired
    public AnimalVisitedLocationPointConverter(@Lazy AnimalVisitedLocationService animalVisitedLocationService, @Lazy AnimalVisitedLocationMapper animalVisitedLocationMapper) {
        this.animalVisitedLocationService = animalVisitedLocationService;
        this.animalVisitedLocationMapper = animalVisitedLocationMapper;
    }

    /**
     * convert list of AnimalVisitedLocation to string
     * @param attribute
     * @return string
     */
    @Override
    public String convertToDatabaseColumn(List<AnimalVisitedLocation> attribute)
    {
        log.info("convert set of animalVisitedLocation to string");
        String animalVisitedLocationIds = "";
        if (attribute!=null)
        {
            if (attribute.size()>0)
            {
                for (AnimalVisitedLocation entity: attribute)
                {
                    animalVisitedLocationIds+=entity.getId()+",";
                }
                animalVisitedLocationIds = animalVisitedLocationIds.substring(0,animalVisitedLocationIds.length()-1);
            }
        }
        log.info("convert string to set of animalVisitedLocation success");
        return animalVisitedLocationIds;
    }

    /**
     * convert string to list of animalVisitedLocation
     * @param dbData
     * @return
     */
    @Transactional
    @Override
    public List<AnimalVisitedLocation> convertToEntityAttribute(String dbData) {
        log.info("convert string to set of animalVisitedLocation");
        List<AnimalVisitedLocation> list = new ArrayList();
        if (dbData!=null)
        {
            String[] array = dbData.split(",");
            for (int i=0; i<array.length; i++)
            {
                if (!array[i].isEmpty())
                {
                    try
                    {
                        Long id = Long.parseLong(array[i]);
                        Optional<AnimalVisitedLocationDto> box = animalVisitedLocationService.findById(id);
                        if (box.isPresent())
                        {
                            list.add(animalVisitedLocationMapper.toEntity(box.get()));
                        }
                        else
                        {
                            log.warn("animal visited location with id {} was not found",id);
                        }
                    }
                    catch (NumberFormatException ex)
                    {
                        log.error("parse exception occurred");
                        list = new ArrayList();
                    }
                    catch (Exception ex)
                    {
                        log.error(ex.getStackTrace().toString());
                        list = new ArrayList();
                    }


                }
            }
        }
        log.info("convert set of animalVisitedLocation to string success");
        return list;
    }

}
