<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Edit Product</title>
<style>

.inputError {
	width: 250px;
	background-color: red;
}


</style>

</head>
<body>

	<h2>Edit Product</h2>
	<div class="inputError">${inputError}</div>

	<form action="update">

		<table>
			<c:if test="${product != null}">


				<tr>
					<td><label>Product Code</label><input type="text" readonly
						name="productCode"
						value="<c:out value='${product.productCode}' />"></td>
				</tr>

				<tr>
					<td><label>Description</label><input type="text"
						name="description"
						value="<c:out value='${product.description}' />"></td>
				</tr>
				<tr>
					<td><label>Price</label><input type="text" name="price"
						value="<c:out value='${product.price}' />"></td>
				</tr>
				<tr>
					<td><label>Release Date</label><input type="datetime-local"
						name="releaseDate"
						value="<c:out value= '${product.releaseDate}' />"></td>
				</tr>
				<tr>
					<td><input type="submit" name="action" value="update" id="button-1" /></td>
					<td><input type="submit" name="action" value="Cancel"
						id="button-1" /></td>
					<td><input type="button" name="action" value="Reset" id="button-1" /></td>
				</tr>
			</c:if>

		</table>


	</form>


</body>
</html>