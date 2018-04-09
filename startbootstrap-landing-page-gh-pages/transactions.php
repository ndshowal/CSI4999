<?php
    include("header.php");
    include("footer.php");
    
    $txusername = $_SESSION['username'];
    
    $query = "select *
            FROM transactions
            WHERE sender='$txusername'
            OR recipient='$txusername'
            ORDER BY start_date";
    $response = @mysqli_query($db, $query);

    if($response) {
        while($row = mysqli_fetch_array($response)) {
            echo '<table class="reviews" align="left" cellspacing="5" cellpadding="8" width="50%">
			
			<td align="left" width="120px"><b>Transaction ID:</b></td>
			<td align="left"><b>Sender:</b></td>
			<td align="left"><b>Recipient:</b></td>
			<td align="left"><b>Transaction Amount:</b></td>
			<td align="left"><b>Memo:</b></td>
			<td align="left"><b>Date:</b></td>';
	
    		while($row = mysqli_fetch_array($response)) {
    		    $txID = $row['transaction_ID'];
        		echo '
        		<tr>
        		    <td align="left"><a href="transaction.php?id='. $txID .'">' . $txID . '</td>
        		    <td align="left">' . $row['sender'].'</td>
        		    <td align="left">' . $row['recipient'] . '</td>
        		    <td align="left">$' . $row['transaction_amount'] . '</td>
	    		    <td align="left">' . $row['memo'] . '</td>
		    	    <td align="left">' . $row['start_date'] . '</td>';
        
        	    echo '</tr>';
    		}

    	echo '</table>';
        }
    }    
?>