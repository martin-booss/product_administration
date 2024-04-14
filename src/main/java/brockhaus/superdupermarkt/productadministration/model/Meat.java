package brockhaus.superdupermarkt.productadministration.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class Meat extends Product{

    @Override
    public boolean hasToBeRemoved() {
        return quality < 40 || this.isExpired();
    }

    @Override
    public double getTotalPrice() {
        double totalPrice = basePrice + 0.2 * quality;
        if(daysUntilExpiration <= 2) {
            totalPrice *= 0.7;
        }
        return totalPrice;
    }

    @Override
    public Meat getProductInNDays(int n) {
        return Meat.builder()
                .name(name)
                .quality(quality - 2 * n)
                .daysUntilExpiration(daysUntilExpiration - n)
                .basePrice(basePrice)
                .build();
    }

}
