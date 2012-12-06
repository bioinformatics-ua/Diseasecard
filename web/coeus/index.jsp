<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <title>COEUS</title>
        <meta name="description" content="COEUS Semantic Web Application Framework">
        <meta name="author" content="Pedro Lopes pedrolopes@ua.pt">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <!-- Le HTML5 shim, for IE6-8 support of HTML elements -->
        <!--[if lt IE 9]>
          <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
        <![endif]-->

        <!-- Le styles -->
        <link href="assets/css/bootstrap.min.css" rel="stylesheet">        
        <link href="assets/css/bootstrap-responsive.min.css" rel="stylesheet">
        <link href="assets/css/docs.css" rel="stylesheet">

        <!-- Le fav and touch icons -->
        <link rel="shortcut icon" href="assets/img/favicon.ico">
    </head>

    <body>        
        <div class="navbar navbar-inverse navbar-fixed-top">
            <div class="navbar-inner">
                <div class="container">
                    <a class="brand" href="#">Diseasecard</a>
                    <div class="nav-collapse collapse">
                        <ul class="nav">
                            <li class="active"><a href="#">Home</a></li>
                            <li><a href="documentation/">Documentation</a></li>
                            <li><a href="science/">Science</a></li>
                            <li><a href="sparqler/">SPARQL</a></li>
                            <li><a data-toggle="modal" data-target="#contact" href="#contact">Contact</a></li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>        

        <!-- contact modal box -->
        <div id="contact" class="modal hide fade" role="dialog" aria-labelledby="contact" aria-hidden="true">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                <h3>Contact Information</h3>
            </div>
            <div class="modal-body">
                <address>
                    <strong>Pedro Lopes</strong><br/>
                    <a href="http://pedrolopes.net" target="_blank">@pedrolopes</a><br/>
                    DETI/IEETA, University of Aveiro<br />
                    Campus Universitário de Santiago<br />
                    3810 - 193 Aveiro<br/>
                    Portugal
                </address>
            </div>
            <div class="modal-footer">
                <a href="mailto:pedrolopes@ua.pt" class="btn btn-primary">Send Mail</a>
            </div>
        </div>

        <div class="jumbotron masthead">
            <div class="container">
                <h1>COEUS</h1>
                <p>Streamlined back-end framework for rapid semantic web application development.</p>
                <p><a href="https://github.com/pdrlps/COEUS/zipball/coeus1.0b" class="btn btn-inverse btn-primary btn-large">Get COEUS</a></p>
                <ul class="masthead-links">
                    <li><a href="https://github.com/pdrlps/COEUS/tree/coeus1.0b" target="_blank">GitHub project</a></li>
                    <li>Version 1.0b</li>
                </ul>
            </div>
        </div>
        <div class="container">
            <div class="marketing">
                <h1>Hello, Semantic Web!</h1>
                <p class="marketing-byline"><em>Ipsa scientia potestas est.</em> Knowledge itself is power.</p>
                <div class="row-fluid">
                    <div class="span4">
                        <img src="assets/img/integrate.png" width="150" height="150" alt="Integration" />
                        <h2>Integration</h2>
                        <p>
                            Create custom warehouses, integrating distributed and heterogeneous data.<br />
                            Integrate CSV, SQL, XML or SPARQL resources with advanced Extract-Transform-Load warehousing features.
                        </p>
                    </div>  
                    <div class="span4">
                        <img src="assets/img/cloud.png" width="150" height="150" alt="Cloud-based" />
                        <h2>Cloud-based</h2>
                        <p>
                            Deploy your knowledgebase in the cloud, using any available host. <br />Your content - available any time, any where. And with full create, read, update, and delete support.
                        </p>
                    </div>   
                    <div class="span4">
                        <img src="assets/img/semantic.png" width="150" height="150" alt="Semantic Web" />
                        <h2>Semantics</h2>
                        <p>
                            Use Semantic Web &amp; LinkedData technologies in all application layers.<br />
                            Enable reasoning and inference over connected knowledge.
                            Access data through with LinkedData interfaces and deliver a custom SPARQL endpoint.
                        </p>
                    </div>    
                </div>
                <div class="row-fluid">

                    <div class="span4">
                        <img src="assets/img/rapid.png" width="150" height="150" alt="RAD" />
                        <h2>Rapid Dev Time</h2>
                        <p>
                            Reduce development time. Get new applications up and running much faster using the latest rapid application development strategies.<br />
                            COEUS is the back-end framework, the client-side is language-agnostic: PHP, Ruby, JavaScript, C#... COEUS' API works everywhere.
                        </p>
                    </div>       
                    <div class="span4">
                        <img src="assets/img/network.png" width="150" height="150" alt="Interoperability" />
                        <h2>Interoperability</h2>
                        <p>
                            Use COEUS advanced API to connect multiple nodes together and with any other software. <br/>
                            Create your own knowledge network using SPARQL Federation enabling data-sharing amongst a scalable number of peers
                        </p>
                    </div>   
                    <div class="span4">
                        <img src="assets/img/distribute.png" width="150" height="150" alt="Ecosystem" />
                        <h2>Ecosystem</h2>
                        <p>
                            Launch your custom application ecosystem. Distribute your data to any platform or device.<br />Reach more users and create new semantic cloud-based software platforms.
                        </p>
                    </div>  
                </div>
                <hr class="soften">
                <h1>Build the future with COEUS</h1>
                <p class="marketing-byline">Semantic Web + Rapid Application Development = Next-Generation Applications.</p>
                <div class="row-fluid">
                    <div class="span4">
                        <h2>What?</h2>
                        <p>COEUS is a next-generation semantic web-powered knowledge management framework. It is targeted at rapid application deployment of new applications in any research field, supported by a comprehensive integration engine and an advanced data distribution API.</p>
                    </div>
                    <div class="span4">
                        <h2>How?</h2>
                        <p>With COEUS you can deploy new semantic warehouses with custom applications - seeds. Each seed provides an API for internal and external usage, enabling the connection of multiple seeds. With multiple seeds connected, their content can be distributed, resulting in a truly semantic knowledge federation network.</p>
                    </div>
                    <div class="span4">
                        <h2>Featured</h2>
                        <a href="http://bioinformatics.ua.pt/dc4/"><img src="assets/img/dc4.png" width="200" height="29" alt="Diseasecard" /></a>
                        A new semantic rare diseases research portal. COEUS is the foundation of the internal application engine, enabling the delivery of advanced UI features and the exploration of collected knowledge programmatically.<br />
                        <p><a class="btn btn-primary btn-inverse" href="http://bioinformatics.ua.pt/dc4/">Go to Diseasecard &raquo;</a></p>
                    </div>

                </div>
            </div>

        </div> 
        <footer class="footer">
            <div class="container">
                <p class="pull-right"><a href="#">Back to top</a></p>

                <p>&copy; <a target="_blank" title="UA.PT Bioinformatics" href="http://bioinformatics.ua.pt/">University of Aveiro</a> 2012</p>
                <p><small>Under Development by <a href="http://pedrolopes.net" target="_blank">@pedrolopes</a></small></p>

                <p> <a href="http://twitter.github.com/bootstrap/" target="_blank"><small>Layout with Twitter Bootstrap</small></a></p>
            </div>
        </footer>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js"></script>
        <script>window.jQuery || document.write('<script src="assets/js/jquery/jquery.min.js"><\/script>')</script>
        <script defer src="assets/js/bootstrap.min.js"></script>       
        <script type="text/javascript">
            var _gaq = _gaq || [];
            _gaq.push(['_setAccount', 'UA-12230872-7']);
            _gaq.push(['_trackPageview']);
            (function() {
                var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
                ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
                var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
            })();
        </script>
        <script>
            $(document).ready(function(){
            });
        </script>
        <!-- Prompt IE 6 users to install Chrome Frame. Remove this if you want to support IE 6.
             chromium.org/developers/how-tos/chrome-frame-getting-started -->
        <!--[if lt IE 7 ]>
          <script src="//ajax.googleapis.com/ajax/libs/chrome-frame/1.0.3/CFInstall.min.js"></script>
          <script>window.attachEvent('onload',function(){CFInstall.check({mode:'overlay'})})</script>
        <![endif]-->
    </body>
</html>
