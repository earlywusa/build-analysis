package com.coding.analysis;

import com.coding.analysis.entity.AnalysisInput;
import com.coding.analysis.entity.MemberAnalysisInput;
import com.coding.analysis.entity.ResultInput;
import com.coding.analysis.entity.ResultState;
import com.coding.common.build.BuildResult;
import com.coding.common.build.Result;
import strman.Strman;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ValidatorImpl implements Validator {

    @Override
    public AnalysisInput validate(BuildResult buildResult) {
        return new AnalysisInput()
                .memberBuildInputs(
                        buildResult.entrySet()
                                .stream()
                                .map(createMemberAnalysisInput())
                                .collect(Collectors.toList())
                );
    }

    private Function<? super Map.Entry<String, List<Result>>, ? extends MemberAnalysisInput> createMemberAnalysisInput() {
        return entry -> new MemberAnalysisInput()
                .qq(entry.getKey())
                .resultInputs(
                        entry.getValue()
                                .stream()
                                .map(
                                        createResultInput()
                                )
                                .collect(Collectors.toList())
                );
    }

    private Function<? super Result, ? extends ResultInput> createResultInput() {
        return result -> {
            ResultInput resultInput = new ResultInput().result(result);
            if (checkNull(result.buildTime, "buildTime", resultInput)) {
                return resultInput;
            }
            ;
            if (checkNull(result.path, "path", resultInput)) {
                return resultInput;
            }
            ;
            if (!result.success && result.specificReason == null) {
                return resultInput
                        .legal(false)
                        .resultState(ResultState.SPECIFIC_REASON_LOSS);
            }
            return resultInput
                    .legal(true)
                    .resultState(ResultState.LEGAL);
        };
    }

    private boolean checkNull(Object reference, String field, ResultInput resultInput) {
        if (reference == null) {
            resultInput
                    .legal(false)
                    .resultState(ResultState.PARAMETER_LOSS)
                    .msg(Strman.append(field, " is null"));

            return true;
        }
        return false;
    }

}
