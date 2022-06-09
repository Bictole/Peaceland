import React, {useState, useEffect} from 'react';
import Typography from '@mui/material/Typography';
import Grid from '@mui/material/Grid';
import Logo from './Logo';
import Alert from '@mui/material/Alert';

function removeID(list, id){
	return list.filter((elem) => elem.id !== id);
}

function AlertScreen(props) {
	const people = [
		{id: 0, name: "Donna McCrohan", peacescore: "-5"},
		{id: 1, name: "Rainer Maria Rilke", peacescore: "-47"},
		{id: 2, name: "Louis Nucera", peacescore: "0"},
		{id: 3, name: "William S. Burroughs", peacescore: "-84"},
	]
	const [peopleList, setPeopleList] = useState(people);
	return (
		<Grid container style={{
			background: "linear-gradient(180deg, rgba(9,9,121,1) 0%, rgba(2,0,36,1) 25%)",
			minHeight: "100vh",
			color: "#FFF",
			textAlign: "center"
		}}>
		  <Grid item lg={12}>
			<Logo width="20vw" color="#FFF"/>
		  </Grid>
		  <Grid item lg={12}>
			  <Typography variant="h2"> PeaceCorp<span style={{color: "yellow"}}>™</span> - The Eye </Typography>
		  </Grid>
		  <Grid item lg={2}/>
		  <Grid item lg={8}>
		  	<Grid container direction={'column'} spacing={2}>
		  		{peopleList.map((person) => (
  			  		<Grid item lg={12}>
  					  	<Alert severity="warning" onClose={() => {setPeopleList(removeID(peopleList, person.id))}}>
  					  		<Typography variant="subtitle1" align="center">
  					  			<strong>[Peaceless individual detected]</strong>  -- The citizen <strong>{person.name}</strong> has an worrying peacescore of <strong>{person.peacescore}.</strong>. 
  					  		</Typography>
  					  	</Alert>
  				  	</Grid>
		  		))}
		  	</Grid>
		  </Grid>
		</Grid>
	);
}

export default AlertScreen;