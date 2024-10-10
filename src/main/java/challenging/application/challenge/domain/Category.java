package challenging.application.challenge.domain;

import challenging.application.exception.challenge.CategoryNotFoundException;

import java.util.Arrays;

public enum Category {

    SPORT("운동", 1),
    SELF_IMPROVEMENT("자기계발", 2),
    HOBBY("취미", 3),
    STUDY("공부", 4);

    private final String categoryName;
    private final Integer categoryCode;

    Category(String categoryName, Integer categoryCode) {
        this.categoryName = categoryName;
        this.categoryCode = categoryCode;
    }

    public static Category findByCategoryCode(Integer code){
        return Arrays.stream(Category.values())
                .filter(category -> category.getCategoryCode().equals(code))
                .findFirst()
                .orElseThrow(CategoryNotFoundException::new);
    }

    public Integer getCategoryCode(){
        return categoryCode;
    }

}
