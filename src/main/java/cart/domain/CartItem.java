package cart.domain;

import cart.exception.CartItemException;

import java.util.List;
import java.util.Objects;

public class CartItem {
	private final Product product;
	private final Member member;
	private Long id;
	private int quantity;
	private boolean checked;

	public CartItem(Member member, Product product) {
		this.quantity = 1;
		this.member = member;
		this.product = product;
		this.checked = true;
	}

	public CartItem(Long id, int quantity, Product product, Member member, final boolean checked) {
		this.id = id;
		this.quantity = quantity;
		this.product = product;
		this.member = member;
		this.checked = checked;
	}

	public Long getId() {
		return id;
	}

	public Member getMember() {
		return member;
	}

	public Product getProduct() {
		return product;
	}

	public int getQuantity() {
		return quantity;
	}

	public boolean isChecked() {
		return checked;
	}

	public void checkOwner(Member member) {
		if (!Objects.equals(this.member.getId(), member.getId())) {
			throw new CartItemException.IllegalMember(this, member);
		}
	}

	public void changeCartItem(final int quantity, final boolean checked) {
		this.quantity = quantity;
		this.checked = checked;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		final CartItem cartItem = (CartItem)o;
		return quantity == cartItem.quantity && checked == cartItem.checked && Objects.equals(id, cartItem.id)
			&& Objects.equals(product, cartItem.product) && Objects.equals(member, cartItem.member);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, quantity, product, member, checked);
	}
}
