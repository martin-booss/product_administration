package brockhaus.superdupermarkt.productadministration.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class Product {

    protected String name;
    protected int quality;
    protected int daysUntilExpiration;
    protected double basePrice;

    public double getTotalPrice() {
        return basePrice + 0.1 * quality;
    }

    public boolean isExpired() {
        return daysUntilExpiration < 0;
    }

    public boolean hasToBeRemoved() {
        return this.isExpired();
    }

    public Product getProductInNDays(int n) {
        return Product.builder()
                .name(name)
                .quality(quality)
                .daysUntilExpiration(daysUntilExpiration - n)
                .basePrice(basePrice)
                .build();
    }

}
