package brockhaus.superdupermarkt.productadministration.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class Cheese extends Product{

    @Override
    public boolean hasToBeRemoved() {
        return quality < 30 || this.isExpired();
    }

    @Override
    public double getTotalPrice() {
        return basePrice + 0.15 * quality;
    }

    @Override
    public Cheese getProductInNDays(int n) {
        return Cheese.builder()
                .name(name)
                .quality(quality - n)
                .daysUntilExpiration(daysUntilExpiration - n)
                .basePrice(basePrice)
                .build();
    }

}
