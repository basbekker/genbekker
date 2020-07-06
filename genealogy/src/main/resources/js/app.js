'use strict';

// tag::vars[]
const React = require('react'); // <1>
const ReactDOM = require('react-dom'); // <2>
const client = require('./client'); // <3>
// end::vars[]

// tag::app[]
class App extends React.Component { 

	constructor(props) {
		super(props);
		this.state = {individuals: []};
	}

	componentDidMount() { 
		client({method: 'GET', path: '/api/individual/all'}).done(response => {
			this.setState({individuals: response.entity._embedded.individuals});
		});
	}

	render() {
		return (
			<IndividualList individuals={this.state.individuals}/>
		)
	}
}
// end::app[]

// tag::individual-list[]
class IndividualList extends React.Component{
	render() {
		const individuals = this.props.individuals.map(individual =>
			<Individual key={individual._links.self.href} individual={individual}/>
		);
		return (
			<table>
				<tbody>
					<tr>
						<th>First Name</th>
						<th>Last Name</th>
						<th>Middle Name</th>
					</tr>
					{individuals}
				</tbody>
			</table>
		)
	}
}
// end::individual-list[]

// tag::individual[]
class Individual extends React.Component{
	render() {
		return (
			<tr>
				<td>{this.props.individual.firstName}</td>
				<td>{this.props.individual.lastName}</td>
				<td>{this.props.individual.middleName}</td>
			</tr>
		)
	}
}
// end::employee[]

// tag::render[]
ReactDOM.render(
	<App />,
	document.getElementById('react')
)
// end::render[]
