package io.github.mskim.comm.cms.util;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * DTO 변환 유틸리티
 *
 * <p>ModelMapper를 래핑하여 간편한 DTO 변환 기능을 제공합니다.</p>
 * <p>엔티티 ↔ DTO 변환 패턴을 표준화합니다.</p>
 *
 * @author CMS Team
 * @since 1.0.0
 */
@Component
@RequiredArgsConstructor
public class DtoMapper {

    private final ModelMapper modelMapper;

    /**
     * 단일 객체 변환
     *
     * @param source 원본 객체
     * @param destinationType 대상 타입
     * @param <S> 원본 타입
     * @param <D> 대상 타입
     * @return 변환된 객체
     */
    public <S, D> D map(S source, Class<D> destinationType) {
        if (source == null) {
            return null;
        }
        return modelMapper.map(source, destinationType);
    }

    /**
     * 리스트 변환
     *
     * @param sourceList 원본 리스트
     * @param destinationType 대상 타입
     * @param <S> 원본 타입
     * @param <D> 대상 타입
     * @return 변환된 리스트
     */
    public <S, D> List<D> mapList(List<S> sourceList, Class<D> destinationType) {
        if (sourceList == null) {
            return null;
        }
        return sourceList.stream()
            .map(source -> map(source, destinationType))
            .collect(Collectors.toList());
    }

    /**
     * 기존 객체에 매핑 (필드 복사)
     *
     * @param source 원본 객체
     * @param destination 대상 객체
     * @param <S> 원본 타입
     * @param <D> 대상 타입
     */
    public <S, D> void map(S source, D destination) {
        if (source == null || destination == null) {
            return;
        }
        modelMapper.map(source, destination);
    }
}
