'use strict';

// tag::vars[]
const React = require('react'); 
const ReactDOM = require('react-dom');
const client = require('./client'); 
// end::vars[]

// tag::report[]
class Report extends React.Component { 

	constructor(props) {
		super(props);
	}

	componentDidMount() { 
		client({method: 'GET', path: '/api/report/offspringreport'}).done(response => {
			this.setState({offspring: response.entity.offspring});
		});
	}

	render() {
		return (
			<div>
				Hello {this.props.offspring}
	        </div>
	    );
	}
	
}
// end::report[]

// tag::render[]
ReactDOM.render(
	<Report />,
	document.getElementById('report')
)
// end::render[]
