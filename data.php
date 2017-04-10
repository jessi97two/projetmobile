<?php
session_start();

	include_once "maLibUtils.php";
	include_once "maLibSQL.pdo.php";
	include_once "maLibSecurisation.php"; 
	include_once "bdd.php";

	$data["connecte"] = valider("connecte","SESSION");
	$data["action"] = valider("action");

	if (!$data["action"])
	{
		// On ne doit rentrer dans le switch que si on y est autorisé
		$data["feedback"] = "Entrez connexion(login,passe) (eg 'user','user')";
	}
	else 
	{
		if($data["action"] == "inscription") {
			if($nom = valider("nom"))
			if($prenom = valider("prenom"))
			if($login = valider("login"))
			if($password = valider("password"))
			if($mail = valider("mail"))
			if($tel = valider("tel")) {
				$ressearch = rechercheUtilisateur($nom,$prenom,$mail,$tel);
				$data["nbcomptelie"] = count($ressearch);
				if(count($ressearch) == 0) {
					$data["idUser"] = ajouterUtilisateur($nom,$prenom,$login,$password,$mail,$tel);
					$data["statutinscription"] = "1"; // 1 signifie que l'inscription a bien été faite
				}
				else {
					$data["statutinscription"] = "0"; // 0 signifie que l'inscription n'a pas été faite car l'user existe déja dans la base
				}
				
			}
			
		}
		else if($data["action"] == "connexion") {
			if($login = valider("login"))
			if($password = valider("password")) {
				$getid = getUser($login,$password);
				if(count($getid) > 0) {
					foreach($getid as $res) {
						$data["idUser"] = $res["id"];
						connectUser($res["id"]);
						$data["connecte"] = "1";
					}
					 
				}
				else {
					$data["idUser"] = "0";
					$data["connecte"] = "0";
				}
			}
		}
		else if($data["action"] == "recherchefriends") {
			if($recherche = valider("search")) {
				$resreq = rechercheUser($recherche);
				$data["countres"] = count($resreq);
				if(count($resreq) > 0) {
					$data["resrecherchefriend"] = $resreq;
				}
			}
		}
		else if($data["action"] == "ajoutami") {
			if($iduser = valider("iduser"))
			if($nom = valider("nom"))
			if($prenom = valider("prenom"))
			if($login = valider("login")) {
				$getid = getidFollower($nom,$prenom,$login);
				if(count($getid) > 0) {
					foreach($getid as $res) {
						$idfollower = $res["id"];
					}
					addFollower($iduser,$idfollower);
					$data["statutajout"] = "1";
				}
				else {
					$data["statutajout"] = "0";
				}
			}
		}
		else if($data["action"] == "getFriends") {
			if($iduser = valider("iduser")) {
				$getfollowers = getFollowers($iduser);
				if(count($getfollowers) > 0) {
					$data["followers"] = $getfollowers;
				}
				else {
					$data["followers"] = "0";
				}
			}
		}
		else if($data["action"] == "getSondagesReceived") {
			if($iduser = valider("iduser")) {
				$sondages = getPools($iduser);
				if(count($sondages)) {
					$data["sondages"] = $sondages;
				}
				else {
					$data["sondages"] = "0";
				}
			}
		}
		else if($data["action"] == "getInvitationsReceived") {
			if($iduser = valider("iduser")) {
				$invitations = getInvitations($iduser);
				if(count($invitations)) {
					$data["invitations"] = $invitations;
				}
				else {
					$data["invitations"] = "0";
				}
			}
		}
		else if($data["action"] == "getEventsProche") {
			if($iduser = valider("iduser")) {
				$events = getEventsProche($iduser);
				if(count($events)) {
					$data["events"] = $events;
				}
				else {
					$data["events"] = "0";
				}
			}
		}
		else if($data["action"] == "getInfosSondage") {
			if($idsondage = valider("idsondage")) {
				$sondage = getInfosSondage($idsondage); 
				if(count($sondage)) {
					$data["sondage"] = $sondage;
					$dates = getPropositionsDatesSondage($idsondage);
					$restaurants = getPropositionsRestaurantSondage($idsondage);
					if(count($dates)) {
						$data["dates"] = $dates;
					}
					else {
						$data["dates"] = "0";
					}
					if(count($restaurants)) {
						$data["restaurants"] = $restaurants;
					}
					else {
						$data["restaurants"] = "0";
					}
				}
				else {
					$data["sondage"] = "0";
				}
			}
		}
		else if($data["action"] == "putReponsesSondage") {
			if($idsondage = valider("idsondage") && $iddate = valider("iddate") && $idresto = valider("idresto") && $iduser = valider("iduser")) {
				addReponseSondage($iduser,$idsondage,$iddate,$idresto);
				$data["statutreponsesondage"] = "1";
			}
			else {
				$data["statutreponsesondage"] = "0";
			}
		}

	}

	echo json_encode($data);
?>