<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Products</title>
</head>
<body>


	<h2>Products</h2>



	<table border="1" >
		<tr>
			<th>Product Code</th>
			<th>Description</th>
			<th>Release Date</th>
			<th>Years Released</th>
			<th>Price</th>
			<th>Action</th>



		</tr>

		<c:forEach var="product" items="${products}">
			<tr>
				<td>${product.productCode}</td>
				<td>${product.description}</td>
				<td>${product.releaseDate}</td>
				<td>${product.yearsReleased}</td>
				<td>${product.price}</td>
				<td><a href="edit?productCode=<c:out value='${product.productCode}' />">Edit</a>

                        <a href="delete?productCode=<c:out value='${product.productCode}' />">Delete</a>  </td>

			</tr>
			
			
		</c:forEach>
		
		




	</table>

	<form action="add">
		<table>
		
			<tr>
			

				<td><input type="submit" value="Add Product" name="addProduct"></td>

			</tr>

		</table>

	</form>
	


</body>
</html>
