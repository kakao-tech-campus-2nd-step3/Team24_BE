package challenging.application.domain.category;

import challenging.application.global.error.ErrorCode;
import challenging.application.global.error.category.CategoryNotFoundException;

import java.util.Arrays;

public enum Category {

    SPORT("운동", 0),
    SELF_IMPROVEMENT("자기계발", 1),
    HOBBY("취미", 2),
    STUDY("공부", 3);

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
                .orElseThrow(() -> new CategoryNotFoundException(ErrorCode.CATEGORY_NOT_FOUND_ERROR));
    }

    public Integer getCategoryCode(){
        return categoryCode;
    }

}
