<?php

/*
Dans ce fichier, on définit diverses fonctions permettant de récupérer des données utiles pour notre projet. 
*/


// inclure ici la librairie faciliant les requêtes SQL
include_once("maLibSQL.pdo.php");

// cette fonction crée un utilisateur 
function ajouterUtilisateur($nom,$prenom,$login,$passe,$mail,$tel)
{
	$SQL = "INSERT INTO users(nom,prenom,login,password,mail,tel) VALUES ('$nom','$prenom','$login','$passe','$mail','$tel')";
	return SQLInsert($SQL);
}

// cette fonction recherche si l'utilisateur voulant créer un compte existe déja
function rechercheUtilisateur($nom,$prenom,$mail,$tel) 
{
	$SQL = "SELECT id FROM users WHERE nom='$nom' AND prenom='$prenom' AND mail='$mail' AND tel='$tel'";
	return parcoursRs(SQLSelect($SQL));
}

// cette fonction récupère l'id de l'utilisateur qui souhaite se connecté
function getUser($login,$passe) {
	$SQL = "SELECT id,nom,prenom FROM users WHERE login='$login' AND password='$passe'";
	return parcoursRs(SQLSelect($SQL));
}

// cette fonction change le boolean et passe l'user en mode connecté
function connectUser($id) {
	$SQL = "UPDATE users SET connecte = 1 WHERE id='$id'";
	SQLUpdate($SQL);
}

function rechercheUser($recherche) {
	$SQL = "SELECT nom,prenom,login FROM users WHERE nom LIKe '%$recherche%' OR prenom LIKE '%$recherche%' OR login LIKE '%$recherche%'";
	return parcoursRs(SQLSelect($SQL));
}

function addFollower($iduser,$idfollower) {
	$SQL = "INSERT INTO followers(iduser,idfollower) VALUES('$iduser','$idfollower')";
	return SQLInsert($SQL);
}

function getidFollower($nom,$prenom,$login) {
	$SQL = "SELECT id FROM users WHERE nom='$nom' AND prenom='$prenom' AND login='$login'";
	return parcoursRs(SQLSelect($SQL));
}

function getFollowers($iduser) {
	$SQL = "SELECT u.nom,u.prenom,u.login FROM followers AS f , users AS u WHERE f.iduser = '$iduser' AND u.id = f.idfollower";
	return parcoursRs(SQLSelect($SQL));
}

function getPools($iduser) {
	$SQL = "SELECT s.titre, s.id FROM sondages AS s
	LEFT JOIN reponsessondages AS r ON r.idsondage = s.id
	WHERE 0 = (SELECT count(*) FROM reponsessondages WHERE iduser='$iduser' AND idsondage=s.id)
	GROUP BY s.titre";
	return parcoursRs(SQLSelect($SQL));
}

function getInvitations($iduser) {
	$SQL = "SELECT i.id, e.nom FROM events AS e 
			LEFT JOIN invitations AS i ON i.idevent = e.id 
			LEFT JOIN groupes AS g ON g.id = i.idgroupe 
			LEFT JOIN liens_users_groupes AS l ON l.idgroupe = g.id 
			WHERE l.iduser = '$iduser'";
	return parcoursRs(SQLSelect($SQL));
}

function getEventsProche($iduser) {
	$SQL = "SELECT e.nom FROM events AS e
			LEFT JOIN participationsevents AS p ON p.idevent = e.id
			WHERE DATEDIFF(e.dateEvent , NOW()) < 7 AND p.iduser = '$iduser'";
	return parcoursRs(SQLSelect($SQL));
}

function getInfosSondage($idsondage) {
	$SQL = "SELECT s.titre, s.descriptif, u.login as sondeur FROM sondages AS s, users AS u 
	WHERE s.id = '$idsondage' AND s.iduser = u.id";
	return parcoursRs(SQLSelect($SQL));
}

function getPropositionsDatesSondage($idsondage) {
	$SQL = "SELECT p.datepropose, p.id FROM propositionsdate AS p
	LEFT JOIN sondages as s ON p.idsondage = s.id
	WHERE s.id = '$idsondage'";
	return parcoursRs(SQLSelect($SQL));
}


function getPropositionsRestaurantSondage($idsondage) {
	$SQL = "SELECT r.nom, r.id FROM propositionsrestaurant AS p
	LEFT JOIN sondages as s ON p.idsondage = s.id
	LEFT JOIN restaurants AS r ON r.id = p.idrestaurant
	WHERE s.id = '$idsondage'";
	return parcoursRs(SQLSelect($SQL));
}

function addReponseSondage($iduser,$idsondage,$iddate,$idresto) {
	$SQL = "INSERT INTO reponsessondages(iduser,idsondage,iddate,idrestaurant) VALUES('$iduser','$idsondage','$iddate','$idresto')";
	return SQLInsert($SQL);
}

/*

SELECT s.titre, s.descriptif, u.login as sondeur, p.datepropose FROM sondages AS s
	LEFT JOIN users AS u ON s.iduser = u.id
	LEFT JOIN propositionsdate as p ON p.idsondage = s.id
	WHERE s.id = 1
*/
?>