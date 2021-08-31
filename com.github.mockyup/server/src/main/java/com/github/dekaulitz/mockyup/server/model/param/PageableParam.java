package com.github.dekaulitz.mockyup.server.model.param;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class PageableParam implements Pageable {

  private static int PAGE_NUMBER = 1;
  private static int PAGE_SIZE = 30;
  private static Sort PAGE_SORT = Sort.unsorted();

  private int page = PAGE_NUMBER;
  private int size = PAGE_SIZE;
  private Sort sort = PAGE_SORT;

  public static String removeToStringNullValues(String lombokToString) {
    //Pattern
    return lombokToString != null ? lombokToString
        .replaceAll("\\@.*?\\,", "")
        .replaceAll("(?<=(, |\\())[^\\s(]+?=null(?:, )?", "")
        .replaceFirst(", \\)$", ")") : null;
  }

  /**
   * Returns the page to be returned.
   *
   * @return the page to be returned.
   */
  @Override
  public int getPageNumber() {
    return page == 0 ? page : page - 1;
  }

  /**
   * Returns the number of items to be returned.
   *
   * @return the number of items of that page
   */
  @Override
  public int getPageSize() {
    return size;
  }

  /**
   * Returns the offset to be taken according to the underlying page and page size.
   *
   * @return the offset to be taken
   */
  @Override
  public long getOffset() {
    return (long) getPageNumber() * (long) this.getPageSize();
  }

  /**
   * Returns the {@link Pageable} requesting the next {@link Page}.
   *
   * @return
   */
  @Override
  public Pageable next() {
    return new PageableParam(getPage() + 1, getSize(), getSort());
  }

  /**
   * Returns the previous {@link Pageable} or the first {@link Pageable} if the current one already
   * is the first one.
   *
   * @return
   */
  @Override
  public Pageable previousOrFirst() {
    return hasPrevious() ? previous() : first();
  }

  public PageableParam previous() {
    return getPage() == 0 ? this
        : new PageableParam(getPageNumber() - 1, getPageSize(), getSort());
  }

  /**
   * Returns the {@link Pageable} requesting the first page.
   *
   * @return
   */
  @Override
  public Pageable first() {
    return new PageableParam(0, getSize(), getSort());
  }

  /**
   * Creates a new {@link Pageable} with {@code pageNumber} applied.
   *
   * @param pageNumber
   * @return a new {@link PageRequest}.
   * @since 2.5
   */
  @Override
  public Pageable withPage(int pageNumber) {
    return new PageableParam(pageNumber, getSize(), getSort());
  }

  /**
   * Returns whether there's a previous {@link Pageable} we can access from the current one. Will
   * return {@literal false} in case the current {@link Pageable} already refers to the first page.
   *
   * @return
   */
  @Override
  public boolean hasPrevious() {
    return this.getPageNumber() > 0;
  }

}
