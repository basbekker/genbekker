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
		this.state = {offspring: []};
	}

	componentDidMount() { 
		client({method: 'GET', path: '/api/report/offspringreport'}).done(response => {
			this.setState({offspring: response.entity.offspring});
		});
	}


	render() {
		return (
			<OffspringList offspringList={this.state.individuals}/>
		)
	}
}
// end::app[]

// tag::individual-list[]
class OffspringList extends React.Component{
	render() {
		const individuals = this.props.individuals.map(individual =>
			<Individual key={individual.self_href} individual={individual}/>
		);
		return (
			<table>
				<tbody>
					<tr>
						<th>Name</th>
						<th>Partner Name</th>
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
				<td>{this.props.individual.name}</td>
				<td>{this.props.individual.partnerName}</td>
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
