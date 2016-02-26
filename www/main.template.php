<!doctype html>
<html>
<head>
<meta charset="utf-8">
<META NAME="language" CONTENT="English">

<title>Rowan Parking Pass Administrator Login</title>
<link rel="stylesheet" type="text/css" href="/css/login.css">
<script type="text/javascript" src="/jquery-1.7.js"></script>
<script type="text/javascript">
<?php echo $jscript; ?>
</script>
</head>

<body>

  <header>
      
  </header>
  <div class="sidebar2">
<aside style='color:#000;'>
    <p><?php
  if(!empty($side)){
    echo $side;
    }
    else{
    if($_SESSION['username']){
    	echo 'logged in as: <a class="headlink" href="home.php">'.$_SESSION['username'].'</a><br /><a href=" logout.php">log out</a>';
    }
    }
    if(!empty($stickyside)){
    echo $stickyside;
    }
    ?></p><br />
  </aside> </div>
  
  <article class="content" style="text-align:center;"><?php
  if(!empty($insert)){
  	echo "<p>".$insert."</p>";
  }
  if(!empty($title)){
    echo "<h1 style='text-align:center'> " . $title . " </h1>";
    }
    ?>
    
    <?php if($links){ foreach($links as $link): ?>
<section class="Message">
<?php if(!empty($link['title'])){
	echo "<h2>" . $link['title'] . "</h2>";
}
echo "<div style='text-align:left'>";
echo $link['msg']; 
?>
</section>
<br /><br />
<?php endforeach; } ?>
  <!-- end .content --></article>
  
  <footer id="bottom" style="text-align: center;">
    <address>
      <a href="mailto:mcconnell93@students.rowan.edu">Email (add yours)</a></a>
    </address>
  </footer>

</body>
</html>