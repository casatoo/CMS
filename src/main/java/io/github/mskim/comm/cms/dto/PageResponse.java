package io.github.mskim.comm.cms.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 페이징 응답 DTO
 * Spring Data의 Page 객체를 클라이언트 친화적인 형태로 변환
 *
 * @param <T> 응답 데이터 타입
 */
@Getter
@Builder
public class PageResponse<T> {

    /**
     * 현재 페이지의 데이터 목록
     */
    private List<T> content;

    /**
     * 현재 페이지 번호 (0부터 시작)
     */
    private int page;

    /**
     * 페이지 크기 (한 페이지당 항목 수)
     */
    private int size;

    /**
     * 전체 데이터 개수
     */
    private long totalElements;

    /**
     * 전체 페이지 수
     */
    private int totalPages;

    /**
     * 첫 번째 페이지 여부
     */
    private boolean first;

    /**
     * 마지막 페이지 여부
     */
    private boolean last;

    /**
     * 정렬 정보 (예: "createdAt: DESC")
     */
    private String sort;

    /**
     * Spring Data Page 객체를 PageResponse로 변환
     *
     * @param page Spring Data Page 객체
     * @param <T> 데이터 타입
     * @return PageResponse 객체
     */
    public static <T> PageResponse<T> from(Page<T> page) {
        return PageResponse.<T>builder()
                .content(page.getContent())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .sort(page.getSort().toString())
                .build();
    }
}
