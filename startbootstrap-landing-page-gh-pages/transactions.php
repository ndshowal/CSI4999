<?php
    include("header.php");
    
    $txusername = $_SESSION['username'];
    
    echo'
    <header class="masthead text-white text-center">
      <div class="overlay"></div>
      <div class="container">
        <h1>Transactions</h1>
      </div>
    </header>
    
    <section class="standard-page transactions">
    <div class="container">
    <div class="row">';
    
    $query = "select *
            FROM transactions
            WHERE sender='$txusername'
            OR recipient='$txusername'
            ORDER BY start_date";
    $response = @mysqli_query($db, $query);

    if($response) {
        echo '<table class="table table-responsive" align="left" cellspacing="5" cellpadding="8" width="50%">
            <thead>
            <tr>
			<td align="left"><b>Transaction ID:</b></td>
			<td align="left"><b>Sender:</b></td>
			<td align="left"><b>Recipient:</b></td>
			<td align="left"><b>Initiated By:</b></td>
			<td align="left"><b>Transaction Amount:</b></td>
			<td align="left"><b>Memo:</b></td>
			<td align="left"><b>Date:</b></td>
			</tr>
			</thead>';
        while($row = mysqli_fetch_array($response)) {
    	    $txID = $row['transaction_ID'];
        	echo '<tr>
                <td align="left"><a href="transaction.php?id='. $txID .'">' . $txID . '</td>
        		<td align="left">' . $row['sender'].'</td>
        		<td align="left">' . $row['recipient'] . '</td>
        		<td align="left">' . $row['initiator'] . '</td>
        		<td align="left">$' . $row['transaction_amount'] . '</td>
	    		<td align="left">' . $row['memo'] . '</td>
		    	<td align="left">' . $row['start_date'] . '</td>';
        
        	echo '</tr>';
    	}

    	echo '</table>';
    }
    echo '</div></div></section>';
    include("footer.php");
  
?>