<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>Search</title>
	<link rel="stylesheet" href="style.css">
	</head>
	<body>
	<h1 align="left">PlumbersByU</h1>
	<table class="navbar">
		<tr>
			<th scope="col"><a href="Home.php">Home</a></th>
			<th scope="col" class="active"><a href="Search.php">Search</a></th>
			<th scope="col"><a href="Profile.php">Profile</a></th>
			<th scope="col"><a href="Login.php">Login</a></th>
		</tr>
	</table>
	
	<h2>Plumbers Registered With Us: </h2>
	
	<ul class="plumber_list">
		<li><a href="Plumber.php?id=1">Nelson Brothers Plumbing & Sewer Inc. </a></li><br>
		<li><a href="Plumber.php?id=2">WaterWorks Plumbing</a></li><br>
		<li><a href="Plumber.php?id=3">P&M Plumbing and Mechanical</a></li><br>
		<li><a href="Plumber.php?id=4">Levine & Sons Plumbing, Heating, and Cooling</a></li><br>
		<li><a href="Plumber.php?id=5">Fuller Plumbing & Heating</a></li><br>
		<li><a href="Plumber.php?id=6">Lawerence Plumbing</a></li><br>
	</ul>
	
	</body>	
	<?php
		session_start();
		$_SESSION['profile_message'] = ""; 
	?>
</html>