package cart.dao;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import cart.exception.notfound.CartItemNotFoundException;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Repository
public class CartItemDao {
	private final JdbcTemplate jdbcTemplate;

	public CartItemDao(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<CartItem> findByMemberId(Long memberId) {
		String sql = "SELECT cart_item.id, cart_item.member_id, cart_item.checked, member.email, product.id, product.name, product.price, product.image_url, cart_item.quantity " +
			"FROM cart_item " +
			"INNER JOIN member ON cart_item.member_id = member.id " +
			"INNER JOIN product ON cart_item.product_id = product.id " +
			"WHERE cart_item.member_id = ?";
		return jdbcTemplate.query(sql, (rs, rowNum) -> {
			String email = rs.getString("email");
			Long productId = rs.getLong("product.id");
			String name = rs.getString("name");
			int price = rs.getInt("price");
			String imageUrl = rs.getString("image_url");
			Long cartItemId = rs.getLong("cart_item.id");
			int quantity = rs.getInt("cart_item.quantity");
			boolean checked = rs.getBoolean("cart_item.checked");
			Member member = new Member(memberId, email, null);
			Product product = new Product(productId, name, price, imageUrl);
			return new CartItem(cartItemId, quantity, product, member, checked);
		}, memberId);
	}

	public List<CartItem> findByMemberIdAndChecked(Long memberId) {
		String sql = "SELECT cart_item.id, cart_item.member_id, cart_item.checked, member.email, product.id, product.name, product.price, product.image_url, cart_item.quantity " +
			"FROM cart_item " +
			"INNER JOIN member ON cart_item.member_id = member.id " +
			"INNER JOIN product ON cart_item.product_id = product.id " +
			"WHERE cart_item.member_id = ? " +
			"AND cart_item.checked = true";
		return jdbcTemplate.query(sql, (rs, rowNum) -> {
			String email = rs.getString("email");
			Long productId = rs.getLong("product.id");
			String name = rs.getString("name");
			int price = rs.getInt("price");
			String imageUrl = rs.getString("image_url");
			Long cartItemId = rs.getLong("cart_item.id");
			int quantity = rs.getInt("cart_item.quantity");
			boolean checked = rs.getBoolean("cart_item.checked");
			Member member = new Member(memberId, email, null);
			Product product = new Product(productId, name, price, imageUrl);
			return new CartItem(cartItemId, quantity, product, member, checked);
		}, memberId);
	}

	public Long save(CartItem cartItem) {
		KeyHolder keyHolder = new GeneratedKeyHolder();

		jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(
				"INSERT INTO cart_item (member_id, product_id, quantity, checked) VALUES (?, ?, ?, ?)",
				Statement.RETURN_GENERATED_KEYS
			);

			ps.setLong(1, cartItem.getMember().getId());
			ps.setLong(2, cartItem.getProduct().getId());
			ps.setInt(3, cartItem.getQuantity());
			ps.setBoolean(4, cartItem.isChecked());

			return ps;
		}, keyHolder);

		return Objects.requireNonNull(keyHolder.getKey()).longValue();
	}

	public CartItem findById(Long id) {
		String sql = "SELECT cart_item.id, cart_item.member_id, cart_item.checked, member.email, product.id, product.name, product.price, product.image_url, cart_item.quantity " +
			"FROM cart_item " +
			"INNER JOIN member ON cart_item.member_id = member.id " +
			"INNER JOIN product ON cart_item.product_id = product.id " +
			"WHERE cart_item.id = ?";
		List<CartItem> cartItems = jdbcTemplate.query(sql, (rs, rowNum) -> {
			Long memberId = rs.getLong("member_id");
			String email = rs.getString("email");
			Long productId = rs.getLong("id");
			String name = rs.getString("name");
			int price = rs.getInt("price");
			String imageUrl = rs.getString("image_url");
			Long cartItemId = rs.getLong("cart_item.id");
			int quantity = rs.getInt("cart_item.quantity");
			boolean checked = rs.getBoolean("cart_item.checked");
			Member member = new Member(memberId, email, null);
			Product product = new Product(productId, name, price, imageUrl);
			return new CartItem(cartItemId, quantity, product, member, checked);
		}, id);
		if (cartItems.isEmpty()) {
			throw new CartItemNotFoundException(id);
		}
		return cartItems.get(0);
	}

	public void deleteById(Long id) {
		String sql = "DELETE FROM cart_item WHERE id = ?";
		final int affected = jdbcTemplate.update(sql, id);
		if (affected == 0) {
			throw new CartItemNotFoundException(id);
		}
	}

	public void update(CartItem cartItem) {
		String sql = "UPDATE cart_item SET quantity = ?, checked = ? WHERE id = ?";
		final int affected = jdbcTemplate.update(sql, cartItem.getQuantity(), cartItem.isChecked(), cartItem.getId());
		if (affected == 0) {
			throw new CartItemNotFoundException(cartItem.getId());
		}
	}

	public void deleteByProductId(final Long productId) {
		String sql = "DELETE FROM cart_item WHERE product_id = ?";
		final int affected = jdbcTemplate.update(sql, productId);
	}

	public void deleteAll(final List<Long> cartItemIds) {
		final String sql = "DELETE FROM cart_item WHERE id = ?";
		jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(final PreparedStatement ps, final int i) throws SQLException {
				ps.setLong(1, cartItemIds.get(i));
			}

			@Override
			public int getBatchSize() {
				return cartItemIds.size();
			}
		});
	}

	public List<CartItem> findByIds(final List<Long> cartItemIds) {
		final String inSql = String.join(",", Collections.nCopies(cartItemIds.size(), "?"));
		String sql = String.format("SELECT cart_item.id, cart_item.member_id, cart_item.checked, member.email, product.id, product.name, product.price, product.image_url, cart_item.quantity " +
			"FROM cart_item " +
			"INNER JOIN member ON cart_item.member_id = member.id " +
			"INNER JOIN product ON cart_item.product_id = product.id " +
			"WHERE cart_item.id IN (%s)", inSql);
		List<CartItem> cartItems = jdbcTemplate.query(sql, (rs, rowNum) -> {
			Long memberId = rs.getLong("member_id");
			String email = rs.getString("email");
			Long productId = rs.getLong("id");
			String name = rs.getString("name");
			int price = rs.getInt("price");
			String imageUrl = rs.getString("image_url");
			Long cartItemId = rs.getLong("cart_item.id");
			int quantity = rs.getInt("cart_item.quantity");
			boolean checked = rs.getBoolean("cart_item.checked");
			Member member = new Member(memberId, email, null);
			Product product = new Product(productId, name, price, imageUrl);
			return new CartItem(cartItemId, quantity, product, member, checked);
		}, cartItemIds.toArray());
		return cartItems;
	}
}
