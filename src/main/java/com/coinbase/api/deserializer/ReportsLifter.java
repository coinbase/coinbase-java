package com.coinbase.api.deserializer;

import java.util.ArrayList;
import java.util.List;

import com.coinbase.api.entity.Report;
import com.coinbase.api.entity.ReportNode;
import com.fasterxml.jackson.databind.util.StdConverter;

public class ReportsLifter extends StdConverter<List<ReportNode>, List<Report>> {

    public List<Report> convert(List<ReportNode> nodes) {
	ArrayList<Report> result = new ArrayList<Report>();
	
	for (ReportNode node : nodes) {
	    result.add(node.getReport());
	}
	
	return result;
    }

}
