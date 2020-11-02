<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Add Product</title>
<style>
.errMsg {
	width: 250px;
	background-color: red;
}

.inputError {
	width: 250px;
	background-color: red;
}

.dateError {
	width: 250px;
	background-color: red;
}

.priceError {
	width: 250px;
	background-color: red;
}
</style>
</head>
<body>

	<h2>Add Product</h2>

	<div class="errMsg">${errMsg}</div>
	<div class="inputError">${inputError}</div>
	<div class="dateError">${dateError}</div>
	<div class="priceError">${priceError}</div>
	<form action="landing" method="post">

		<table>


			<tr>
				<td><label>Product Code<input type="text"
						name="productCode" value="${param.productCode}"></label></td>
			</tr>

			<tr>
				<td><label>Description<input type="text"
						name="description" value="${param.description}"></label></td>
			</tr>
			<tr>
				<td><label>Price<input type="text" name="price"
						value="${param.price}"></label></td>
			</tr>
			<tr>
				<td><label>Release Date<input type="datetime-local"
						placeholder="YYYY-MM-DD"
						pattern="[0-9]{4}-(0[1-9]|1[012])-(0[1-9]|1[0-9]|2[0-9]|3[01])"
						name="releaseDate" value="${param.releaseDate}"></label></td>
			</tr>

			<tr>

				<td><input type="submit" name="action" value="Add" id="button-1" /></td>
				<td><input type="submit" name="action" value="Cancel"
					id="button-1" /></td>
				<td><input type="reset" value="Reset" id="button-1" /></td>
			</tr>

		</table>


	</form>



</body>
</html>