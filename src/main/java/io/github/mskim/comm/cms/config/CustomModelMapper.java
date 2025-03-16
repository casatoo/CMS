package io.github.mskim.comm.cms.config;

import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

public class CustomModelMapper {

    private static final ModelMapper modelMapper = new ModelMapper();

    private CustomModelMapper() {
        // 인스턴스 생성 방지
    }

    public static ModelMapper getInstance() {
        return modelMapper;
    }

    public static <S, D> List<D> mapList(List<S> source, Class<D> destinationType) {
        return source.stream()
                .map(item -> modelMapper.map(item, destinationType))
                .collect(Collectors.toList());
    }
}
