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
			if($idsondage = valider("idsondage"))
			if($iddate = valider("iddate"))
			if($idresto = valider("idresto"))
			if($iduser = valider("iduser")) {
				addReponseSondage($iduser,$idsondage,$iddate,$idresto);
				$data["statutreponsesondage"] = "1";
			}
			else {
				$data["statutreponsesondage"] = "0";
			}
		}
		else if($data["action"] == "getInfosInvitation") {
			if($idinvitation = valider("idinvitation")) {
				$invitation = getInfosInvitation($idinvitation);
				if(count($invitation)) {
					$data["invitation"] = $invitation;
				}
				else {
					$data["invitation"] = "0";
				}
			}
		}
		else if($data["action"] == "putReponsesInvitation") {
			if($iduser = valider("iduser"))
			if($idevent = valider("idevent"))
			if($reponseinvit = valider("reponseinvit")) {
				putReponseInvitation($iduser,$idevent,$reponseinvit);
				$data["statutreponseinvitation"] = "1";
				$data["iduser"] = $iduser;
				$data["idevent"] = $idevent;
				$data["reponseinvit"] = $reponseinvit;
			}
			else {
				$data["statutreponseinvitation"] = "0";
			}
		}
		else if($data["action"] == "addGroup") {
			if($iduser = valider("iduser"))
			if($groupidphone = valider("groupidphone"))
			if($group = valider("groupName")) {
				$groupe = addGroup($iduser,$group,$groupidphone);
				$data["groupe"] = $groupe;
			}
			else {
				$data["groupe"] = "0";
			}

		}
		else if($data["action"] == "removeGroup") {
			if($iduser = valider("iduser"))
			if($group = valider("groupName")) {
				removeGroup($iduser,$group);
				$data["groupe"] = "1";
			}

		}
		else if($data["action"] == "getGroups") {
			if($iduser = valider("iduser")) {
				$groups = getGroups($iduser);
				if(count($groups)) {
					$data["groupes"] = $groups;
				}
				else {
					$data["groupes"] = "0";
				}
			}
		}
		else if($data["action"] == "getGroupById") {
			if($idgroup = valider("idgroup")) {
				$group = getGroupById($idgroup);
				if(count($group)) {
					$data["groupe"] = $group;
				}
				else {
					$data["groupe"] = "0";
				}
			}
		}
		else if($data["action"] == "getRestaurants") {
			if($iduser = valider("iduser")) {
				$restaurants = getRestaurants($iduser);
				if(count($restaurants)) {
					$data["restaurants"] = $restaurants;
				}
				else {
					$data["restaurants"] = "0";
				}
			}
		}
		else if($data["action"] == "getRestaurantById") {
			if($idrestaurant = valider("idrestaurant")) {
				$restaurant = getRestaurantById($idrestaurant);
				if(count($restaurant)) {
					$data["restaurant"] = $restaurant;
				}
				else {
					$data["restaurant"] = "0";
				}
			}
		}
		else if($data["action"] == "addSondage") {
			if($iduser = valider("iduser"))
			if($titre = valider("titre"))
			if($description = valider("description"))
			if($groupe = valider("groupe")) {
				$titremodif = str_replace("__"," ",$titre);
				$descriptionmodif = str_replace("__"," ",$description);
				$idsondage = addSondage($iduser,$titremodif,$descriptionmodif,$groupe);
				$data["sondage"] = $idsondage;
			}
			else {
				$data["sondage"] = "0";
			}
		}
		else if($data["action"] == "addProprositionRestaurant") {
			if($idsondage = valider("idsondage"))
			if($idrestaurant = valider("idrestaurant")) {
				$propositionrestaurant = addPropositionRestaurant($idsondage, $idrestaurant);
				$data["propositionrestaurant"] = $propositionrestaurant;
			}
		}
		else if($data["action"] == "addProprositionDate") {
			if($idsondage = valider("idsondage"))
			if($datepropose = valider("datepropose")) {
				$propositiondate = addPropositionDate($idsondage,$datepropose);
				$data["propositiondate"] = $propositiondate;
			}
		}
		else if($data["action"] == "getContactByNumber") {
			if($iduser = valider("iduser"))
			if($numero = valider("numero"))
			if($name = valider("name"))
			if($groupe = valider("groupe")) {
				$contact = getContactByNumber($numero);
				if(count($contact) > 0) {
					foreach($contact as $info) {
						$idcontact = $info["id"]; // recuperer id du contact
					}
					$lien = getLienContactGroup($idcontact,$groupe); // vérifie si le contact est déja lié au groupe
					if(count($lien) > 0) {
						// le contact existait déja dans le groupe en base
						$data["contact"] = "-1";
					}
					else {
						$lienadded = addLienContactGroupe($idcontact,$groupe); // ajout du contact dans la base
						$data["contact"] = $lienadded;
					}
				}
				else {
					$data["contact"] = "-2"; // signifie que le contact n'existe pas dans les users
				}
			}
		}
		else if($data["action"] == "getIdOnTelGroup") {
			if($idgroupe = valider("idgroup")) {
				$id = getInOnTelGroup($idgroupe);
				if(count($id)) {
					$data["idontelgroupe"] = $id;
				}
				else {
					$data["idontelgroupe"] = "0";
				}
			}
		}
		else if($data["action"] == "getAllSondagesUser") {
			if($iduser = valider("iduser")) {
				$sondages = getAllSondagesUser($iduser);
				if(count($sondages)) {
					$data["sondages"] = $sondages;
				}
				else {
					$data["sondages"] = "0";
				}
			}
		}
		else if($data["action"] == "getResultatsDateSondage") {
			if($idsondage = valider("idsondage")) {
				$res = getResultatsDateSondage($idsondage);
				if(count($res)) {
					$data["resultatsDate"] = $res;
				}
				else {
					$data["resultatsDate"] = "0";
				}
			}

		}
		else if($data["action"] == "getUsersResultatsDateSondage") {
			if($idsondage = valider("idsondage"))
			if($iddate = valider("iddate")) {
				$res = getUsersResultatsDateSondage($idsondage,$iddate);
				if(count($res)) {
					$data["resultatsUsersDate"] = $res;
				}
				else {
					$data["resultatsUsersDate"] = "0";
				}
			}
		}
		else if($data["action"] == "getResultatsRestaurantSondage") {
			if($idsondage = valider("idsondage")) {
				$res = getResultatsRestaurantSondage($idsondage);
				if(count($res)) {
					$data["resultatsRestaurant"] = $res;
				}
				else {
					$data["resultatsRestaurant"] = "0";
				}
			}
		}

		else if($data["action"] == "getUsersResultatsRestaurantSondage") {
			if($idsondage = valider("idsondage"))
			if($idrestaurant = valider("idrestaurant")) {
				$res = getUsersResultatsRestaurantSondage($idsondage,$idrestaurant);
				if(count($res)) {
					$data["resultatsUsersRestaurant"] = $res;
				}
				else {
					$data["resultatsUsersRestaurant"] = "0";
				}
			}
		}
		else if($data["action"] == "getGroupByIdSondage") {
			if($idsondage = valider("idsondage")) {
				$group = getGroupByIdSondage($idsondage);
				if(count($group)) {
					$data["groupname"] = $group;
				}
				else {
					$data["groupname"] = "0";
				}
			}
		}
		else if($data["action"] == "getRestaurantBySondageName") {
			if($idsondage = valider("idsondage"))
			if($restaurant = valider("nom")) {
				$resto = str_replace("__"," ",$restaurant);
				$inforestaurant = getRestaurantBySondageName($idsondage,$resto);
				if(count($inforestaurant)) {
					$data["idrestaurant"] = $inforestaurant;
				}
				else {
					$data["idrestaurant"] = "0";
					$data["idsondage"] = $idsondage;
					$data["resto"] = $resto;
				}
			}
		}
		else if($data["action"] == "addEvent") {
			if($titre = valider("nom"))
			if($dateevent = valider("dateEvent"))
			if($heure = valider("heureEvent"))
			if($restaurant = valider("restaurant"))
			if($iduser = valider("iduser"))
			if($idgroupe = valider("idgroupe")) {
				$nom = str_replace("__", " ", $titre);
				$event = addEvent($nom,$dateevent,$heure,$restaurant,$iduser,$idgroupe);
				$data["event"] = $event;
			}
			else {
				$data["event"] = "0";
			}
		}
		else if($data["action"] == "archiverSondage") {
			if($idsondage = valider("idsondage")) {
				archiverSondage($idsondage);
			}
		}
		else if($data["action"] == "getAllEventsUser") {
			if($iduser = valider("iduser")) {
				$sondages = getAllEventsUser($iduser);
				if(count($sondages)) {
					$data["events"] = $sondages;
				}
				else {
					$data["events"] = "0";
				}
			}
		}
		else if($data["action"] == "getInfosEvent") {
			if($idevent = valider("idevent")) {
				$event = getInfosEvent($idevent);
				if(count($event)) {
					$data["event"] = $event;
				}
				else {
					$data["event"] = "0";
				}
			}
		}
		else if($data["action"] == "getReponsesUsersEvent") {
			if($idevent = valider("idevent")) {
				$reponsesevent = getReponsesUsersEvent($idevent);
				if(count($reponsesevent)) {
					$data["reponsesevent"] = $reponsesevent;
				}
				else {
					$data["reponsesevent"] = "0";
				}
			}
		}
		else if($data["action"] == "removeLiensGroupContacts") {
			
		}
	}

	echo json_encode($data);
?>