package brockhaus.superdupermarkt.productadministration.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class Wine extends Product {

    @Override
    public double getTotalPrice() {
        return basePrice;
    }

    @Override
    public boolean isExpired() {
        return false;
    }

    @Override
    public Wine getProductInNDays(int n) {
        int newQuality = quality + (int) (n * 0.1);
        if(newQuality > 50) {
            newQuality = 50;
        }

        return Wine.builder()
                .name(name)
                .quality(newQuality)
                .daysUntilExpiration(daysUntilExpiration)
                .basePrice(basePrice)
                .build();
    }

}
