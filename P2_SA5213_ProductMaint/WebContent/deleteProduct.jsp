<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Delete Product</title>
</head>
<body>

	<h2>Delete Product</h2>

	<form action="deleteAction">

		<table>

			<c:if test="${product != null}">
				<tr>
					<td><label>Product Code<input type="text"
							name="productCode" value="${product.productCode}" /></label></td>
				</tr>

				<tr>
					<td><label>Description<input type="text"
							name="description" value="${product.description}"></label></td>
				</tr>
				<tr>
					<td><label>Price<input type="text" name="price"
							value="${product.price}"></label></td>
				</tr>
				<tr>
					<td><label>Release Date<input type="datetime-local"
							name="releaseDate" value="${product.releaseDate}"></label></td>
				</tr>
				<tr>
					<td><input type="submit" name="action" value="Confirm Delete" id="button-1" onclick="return confirm('Are you sure you want to delete?')"/></td>
					<td><input type="submit" name="action" value="Cancel"
						id="button-1" /></td>
					<td><input type="reset" name="action" value="Reset" id="button-1" /></td>
				</tr>
			</c:if>

		</table>


	</form>


</body>
</html>